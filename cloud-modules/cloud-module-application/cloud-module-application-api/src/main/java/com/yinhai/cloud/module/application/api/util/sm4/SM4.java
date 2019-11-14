package com.yinhai.cloud.module.application.api.util.sm4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SM4
{
	public static final int SM4_ENCRYPT = 1;

	public static final int SM4_DECRYPT = 0;

	private long GET_ULONG_BE(byte[] b, int i)
	{
		long n = (long)(b[i] & 0xff) << INT_24 | (long)((b[i + 1] & 0xff) << INT_16) | (long)((b[i + INT_2] & 0xff) << INT_8) | (long)(b[i + INT_3] & 0xff) & 0xffffffffL;
		return n;
	}

	private void PUT_ULONG_BE(long n, byte[] b, int i)
	{
		b[i] = (byte)(int)(0xFF & n >> INT_24);
        b[i + 1] = (byte)(int)(0xFF & n >> INT_16);
        b[i + INT_2] = (byte)(int)(0xFF & n >> INT_8);
        b[i + INT_3] = (byte)(int)(0xFF & n);
	}

	private long SHL(long x, int n)
	{
		return (x & 0xFFFFFFFF) << n;
	}

	private long ROTL(long x, int n)
	{
		return SHL(x, n) | x >> (INT_32 - n);
	}

	private void SWAP(long[] sk, int i)
	{
		long t = sk[i];
		sk[i] = sk[(INT_31 - i)];
		sk[(INT_31 - i)] = t;
	}

	 private static final byte[] SboxTable = { (byte) 0xd6, (byte) 0x90, (byte) 0xe9, (byte) 0xfe,
		(byte) 0xcc, (byte) 0xe1, 0x3d, (byte) 0xb7, 0x16, (byte) 0xb6,
		0x14, (byte) 0xc2, 0x28, (byte) 0xfb, 0x2c, 0x05, 0x2b, 0x67,
		(byte) 0x9a, 0x76, 0x2a, (byte) 0xbe, 0x04, (byte) 0xc3,
		(byte) 0xaa, 0x44, 0x13, 0x26, 0x49, (byte) 0x86, 0x06,
		(byte) 0x99, (byte) 0x9c, 0x42, 0x50, (byte) 0xf4, (byte) 0x91,
		(byte) 0xef, (byte) 0x98, 0x7a, 0x33, 0x54, 0x0b, 0x43,
		(byte) 0xed, (byte) 0xcf, (byte) 0xac, 0x62, (byte) 0xe4,
		(byte) 0xb3, 0x1c, (byte) 0xa9, (byte) 0xc9, 0x08, (byte) 0xe8,
		(byte) 0x95, (byte) 0x80, (byte) 0xdf, (byte) 0x94, (byte) 0xfa,
		0x75, (byte) 0x8f, 0x3f, (byte) 0xa6, 0x47, 0x07, (byte) 0xa7,
		(byte) 0xfc, (byte) 0xf3, 0x73, 0x17, (byte) 0xba, (byte) 0x83,
		0x59, 0x3c, 0x19, (byte) 0xe6, (byte) 0x85, 0x4f, (byte) 0xa8,
		0x68, 0x6b, (byte) 0x81, (byte) 0xb2, 0x71, 0x64, (byte) 0xda,
		(byte) 0x8b, (byte) 0xf8, (byte) 0xeb, 0x0f, 0x4b, 0x70, 0x56,
		(byte) 0x9d, 0x35, 0x1e, 0x24, 0x0e, 0x5e, 0x63, 0x58, (byte) 0xd1,
		(byte) 0xa2, 0x25, 0x22, 0x7c, 0x3b, 0x01, 0x21, 0x78, (byte) 0x87,
		(byte) 0xd4, 0x00, 0x46, 0x57, (byte) 0x9f, (byte) 0xd3, 0x27,
		0x52, 0x4c, 0x36, 0x02, (byte) 0xe7, (byte) 0xa0, (byte) 0xc4,
		(byte) 0xc8, (byte) 0x9e, (byte) 0xea, (byte) 0xbf, (byte) 0x8a,
		(byte) 0xd2, 0x40, (byte) 0xc7, 0x38, (byte) 0xb5, (byte) 0xa3,
		(byte) 0xf7, (byte) 0xf2, (byte) 0xce, (byte) 0xf9, 0x61, 0x15,
		(byte) 0xa1, (byte) 0xe0, (byte) 0xae, 0x5d, (byte) 0xa4,
		(byte) 0x9b, 0x34, 0x1a, 0x55, (byte) 0xad, (byte) 0x93, 0x32,
		0x30, (byte) 0xf5, (byte) 0x8c, (byte) 0xb1, (byte) 0xe3, 0x1d,
		(byte) 0xf6, (byte) 0xe2, 0x2e, (byte) 0x82, 0x66, (byte) 0xca,
		0x60, (byte) 0xc0, 0x29, 0x23, (byte) 0xab, 0x0d, 0x53, 0x4e, 0x6f,
		(byte) 0xd5, (byte) 0xdb, 0x37, 0x45, (byte) 0xde, (byte) 0xfd,
		(byte) 0x8e, 0x2f, 0x03, (byte) 0xff, 0x6a, 0x72, 0x6d, 0x6c, 0x5b,
		0x51, (byte) 0x8d, 0x1b, (byte) 0xaf, (byte) 0x92, (byte) 0xbb,
		(byte) 0xdd, (byte) 0xbc, 0x7f, 0x11, (byte) 0xd9, 0x5c, 0x41,
		0x1f, 0x10, 0x5a, (byte) 0xd8, 0x0a, (byte) 0xc1, 0x31,
		(byte) 0x88, (byte) 0xa5, (byte) 0xcd, 0x7b, (byte) 0xbd, 0x2d,
		0x74, (byte) 0xd0, 0x12, (byte) 0xb8, (byte) 0xe5, (byte) 0xb4,
		(byte) 0xb0, (byte) 0x89, 0x69, (byte) 0x97, 0x4a, 0x0c,
		(byte) 0x96, 0x77, 0x7e, 0x65, (byte) 0xb9, (byte) 0xf1, 0x09,
		(byte) 0xc5, 0x6e, (byte) 0xc6, (byte) 0x84, 0x18, (byte) 0xf0,
		0x7d, (byte) 0xec, 0x3a, (byte) 0xdc, 0x4d, 0x20, 0x79,
		(byte) 0xee, 0x5f, 0x3e, (byte) 0xd7, (byte) 0xcb, 0x39, 0x48 };

	 private static final int[] FK = { 0xa3b1bac6, 0x56aa3350, 0x677d9197, 0xb27022dc };

	 private static final int[] CK = { 0x00070e15,0x1c232a31,0x383f464d,0x545b6269,
		0x70777e85,0x8c939aa1,0xa8afb6bd,0xc4cbd2d9,
		0xe0e7eef5,0xfc030a11,0x181f262d,0x343b4249,
		0x50575e65,0x6c737a81,0x888f969d,0xa4abb2b9,
		0xc0c7ced5,0xdce3eaf1,0xf8ff060d,0x141b2229,
		0x30373e45,0x4c535a61,0x686f767d,0x848b9299,
		0xa0a7aeb5,0xbcc3cad1,0xd8dfe6ed,0xf4fb0209,
		0x10171e25,0x2c333a41,0x484f565d,0x646b7279 };

	private byte sm4Sbox(byte inch)
	{
		int i = inch & 0xFF;
		byte retVal = SboxTable[i];
		return retVal;
	}

	private long sm4Lt(long ka)
	{
		long bb;
		long c;
		byte[] a = new byte[INT_4];
		byte[] b = new byte[INT_4];
		PUT_ULONG_BE(ka, a, 0);
		b[0] = sm4Sbox(a[0]);
		b[1] = sm4Sbox(a[1]);
		b[INT_2] = sm4Sbox(a[INT_2]);
		b[INT_3] = sm4Sbox(a[INT_3]);
		bb = GET_ULONG_BE(b, 0);
		c = bb ^ ROTL(bb, INT_2) ^ ROTL(bb, INT_10) ^ ROTL(bb, INT_18) ^ ROTL(bb, INT_24);
		return c;
	}

	private long sm4F(long x0, long x1, long x2, long x3, long rk)
	{
		return x0 ^ sm4Lt(x1 ^ x2 ^ x3 ^ rk);
	}

	private long sm4CalciRK(long ka)
	{
		long bb;
		long rk;
		byte[] a = new byte[INT_4];
		byte[] b = new byte[INT_4];
		PUT_ULONG_BE(ka, a, 0);
		b[0] = sm4Sbox(a[0]);
		b[1] = sm4Sbox(a[1]);
		b[INT_2] = sm4Sbox(a[INT_2]);
		b[INT_3] = sm4Sbox(a[INT_3]);
		bb = GET_ULONG_BE(b, 0);
		rk = bb ^ ROTL(bb, INT_13) ^ ROTL(bb, INT_23);
		return rk;
	}

	private void sm4_setkey(long[] SK, byte[] key)
	{
		long[] MK = new long[INT_4];
		long[] k = new long[INT_36];
		int i = 0;
		MK[0] = GET_ULONG_BE(key, 0);
		MK[1] = GET_ULONG_BE(key, INT_4);
		MK[INT_2] = GET_ULONG_BE(key, INT_8);
		MK[INT_3] = GET_ULONG_BE(key, INT_12);
		k[0] = MK[0] ^ (long) FK[0];
		k[1] = MK[1] ^ (long) FK[1];
		k[INT_2] = MK[INT_2] ^ (long) FK[INT_2];
		k[INT_3] = MK[INT_3] ^ (long) FK[INT_3];
		for (; i < INT_32; i++)
		{
			k[(i + INT_4)] = (k[i] ^ sm4CalciRK(k[(i + 1)] ^ k[(i + INT_2)] ^ k[(i + INT_3)] ^ (long) CK[i]));
			SK[i] = k[(i + INT_4)];
		}
	}

	private void sm4_one_round(long[] sk, byte[] input, byte[] output)
	{
		int i = 0;
		long[] ulbuf = new long[INT_36];
		ulbuf[0] = GET_ULONG_BE(input, 0);
		ulbuf[1] = GET_ULONG_BE(input, INT_4);
		ulbuf[INT_2] = GET_ULONG_BE(input, INT_8);
		ulbuf[INT_3] = GET_ULONG_BE(input, INT_12);
		while (i < INT_32)
		{
			ulbuf[(i + INT_4)] = sm4F(ulbuf[i], ulbuf[(i + 1)], ulbuf[(i + INT_2)], ulbuf[(i + INT_3)], sk[i]);
			i++;
		}
		PUT_ULONG_BE(ulbuf[INT_35], output, 0);
		PUT_ULONG_BE(ulbuf[INT_34], output, INT_4);
		PUT_ULONG_BE(ulbuf[INT_33], output, INT_8);
		PUT_ULONG_BE(ulbuf[INT_32], output, INT_12);
	}

	private byte[] padding(byte[] input, int mode)
	{
		if (input == null)
		{
			return null;
		}

		byte[] ret;
		if (mode == SM4_ENCRYPT)
		{
			int p = INT_16 - input.length % INT_16;
			ret = new byte[input.length + p];
			System.arraycopy(input, 0, ret, 0, input.length);
			for (int i = 0; i < p; i++)
			{
				ret[input.length + i] = (byte) p;
			}
		}
		else
		{
			int p = input[input.length - 1];
			ret = new byte[input.length - p];
			System.arraycopy(input, 0, ret, 0, input.length - p);
		}
		return ret;
	}

	public void sm4_setkey_enc(SM4Context ctx, byte[] key) throws Exception
	{
		if (ctx == null)
		{
			throw new Exception("ctx is null!");
		}

		if (key == null || key.length != INT_16)
		{
			throw new Exception("key error!");
		}

		ctx.setMode(SM4_ENCRYPT);
		sm4_setkey(ctx.getSk(), key);
	}

	public void sm4_setkey_dec(SM4Context ctx, byte[] key) throws Exception
	{
		if (ctx == null)
		{
			throw new Exception("ctx is null!");
		}

		if (key == null || key.length != INT_16)
		{
			throw new Exception("key error!");
		}

		int i = 0;
		ctx.setMode(SM4_DECRYPT);
		sm4_setkey(ctx.getSk(), key);
		for (i = 0; i < INT_16; i++)
		{
			SWAP(ctx.getSk(), i);
		}
	}

	public byte[] sm4_crypt_ecb(SM4Context ctx, byte[] input) throws Exception
	{
		if (input == null)
		{
			throw new Exception("input is null!");
		}

		if ((ctx.getPadding()) && (ctx.getMode() == SM4_ENCRYPT))
		{
			input = padding(input, SM4_ENCRYPT);
		}

		int length = input.length;
		ByteArrayInputStream bins = new ByteArrayInputStream(input);
		ByteArrayOutputStream bous = new ByteArrayOutputStream();
		for(; length > 0; length -= INT_16)
		{
			byte[] in = new byte[INT_16];
			byte[] out = new byte[INT_16];
			boolean flag = true;
			while(flag && bins.read(in)>0){
				flag = false;
			}
			sm4_one_round(ctx.getSk(), in, out);
			bous.write(out);
		}

		byte[] output = bous.toByteArray();
		if (ctx.getPadding() && ctx.getMode() == SM4_DECRYPT)
		{
			output = padding(output, SM4_DECRYPT);
		}
		bins.close();
		bous.close();
		return output;
	}

	public byte[] sm4_crypt_cbc(SM4Context ctx, byte[] iv, byte[] input) throws Exception
	{
		if (iv == null || iv.length != INT_16)
		{
			throw new Exception("iv error!");
		}

		if (input == null)
		{
			throw new Exception("input is null!");
		}

		if (ctx.getPadding() && ctx.getMode() == SM4_ENCRYPT)
		{
			input = padding(input, SM4_ENCRYPT);
		}

		int i = 0;
		int length = input.length;
		ByteArrayInputStream bins = new ByteArrayInputStream(input);
		ByteArrayOutputStream bous = new ByteArrayOutputStream();
		if (ctx.getMode() == SM4_ENCRYPT)
		{
			for(; length > 0; length -= INT_16)
			{
				byte[] in = new byte[INT_16];
				byte[] out = new byte[INT_16];
				byte[] out1 = new byte[INT_16];

				boolean flag = true;
				while(flag && bins.read(in)>0){
					flag = false;
				}

				for (i = 0; i < INT_16; i++)
				{
					out[i] = ((byte) (in[i] ^ iv[i]));
				}
				sm4_one_round(ctx.getSk(), out, out1);
				System.arraycopy(out1, 0, iv, 0, INT_16);
				bous.write(out1);
			}
		}
		else
		{
			byte[] temp = new byte[INT_16];
			for(; length > 0; length -= INT_16)
			{
				byte[] in = new byte[INT_16];
				byte[] out = new byte[INT_16];
				byte[] out1 = new byte[INT_16];

				boolean flag = true;
				while(flag && bins.read(in)>0){
					flag = false;
				}

				System.arraycopy(in, 0, temp, 0, INT_16);
				sm4_one_round(ctx.getSk(), in, out);
				for (i = 0; i < INT_16; i++)
				{
					out1[i] = ((byte) (out[i] ^ iv[i]));
				}
				System.arraycopy(temp, 0, iv, 0, INT_16);
				bous.write(out1);
			}
		}

		byte[] output = bous.toByteArray();
		if (ctx.getPadding() && ctx.getMode() == SM4_DECRYPT)
		{
			output = padding(output, SM4_DECRYPT);
		}
		bins.close();
		bous.close();
		return output;
	}

	private static final int INT_2 = 2;
	private static final int INT_3 = 3;
	private static final int INT_4 = 4;
	private static final int INT_8 = 8;
	private static final int INT_10 = 10;
	private static final int INT_12 = 12;
	private static final int INT_13 = 13;
	private static final int INT_16 = 16;
	private static final int INT_18 = 18;
	private static final int INT_23 = 23;
	private static final int INT_24 = 24;
	private static final int INT_31 = 31;
	private static final int INT_32 = 32;
	private static final int INT_33 = 33;
	private static final int INT_34 = 34;
	private static final int INT_35 = 35;
	private static final int INT_36 = 36;
}
