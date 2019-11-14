package com.yinhai.cloud.core.api.ssh;

import java.io.File;

/**
 * 正斜杠组成的文件路径，与URI路径的路径分隔符一样为“/”
 *
 * @author: zhaokai
 * @create: 2018-09-07 15:19:10
 */
public class SlashPath {
    public static final String PATH_SEPARATOR = "/";
    private String path;

    public SlashPath(String absolutePath) {
        init(absolutePath);
    }

    public SlashPath(String parentAbsolutePath, String subPath) {
        init(parentAbsolutePath + PATH_SEPARATOR + subPath);
    }

    public SlashPath(SlashPath parentPath, String subPath) {
        init(parentPath + PATH_SEPARATOR + subPath);
    }

    @Override
    public String toString() {
        return getFullPath();
    }

    private void init(String absolutePath) {
        if (absolutePath == null || "".equals(absolutePath)) {
            throw new IllegalArgumentException("Path [" + absolutePath + "] cat't be empty!");
        }
        if (!absolutePath.contains(":")) {
            // Unix 路径
            if (!absolutePath.trim().startsWith(PATH_SEPARATOR)) {
                throw new IllegalArgumentException("Unix Path [" + absolutePath + "] must be an absolute path ! start with " + PATH_SEPARATOR);
            }
        }
        // 替换Window 路径分隔符，去除多余 /
        this.path = absolutePath.trim().replace("\\", "/").replaceAll("/+", "/");
    }

    public SlashPath(File file) {
        init(file.getAbsolutePath());
    }

    public String getFullPath() {
        return this.path;
    }

    public String getParentPath() {
        if (PATH_SEPARATOR.equals(path)) {
            return PATH_SEPARATOR;
        }
        if (path.lastIndexOf('/') == 0) {
            return PATH_SEPARATOR;
        }
        if (path.endsWith(PATH_SEPARATOR)) {
            path = path.substring(0, path.length() - 1);
        }
        return path.substring(0, path.lastIndexOf(PATH_SEPARATOR));
    }

    public String getName() {
        if (PATH_SEPARATOR.equals(path)) {
            return PATH_SEPARATOR;
        }
        if (path.endsWith(PATH_SEPARATOR)) {
            path = path.substring(0, path.length() - 1);
        }
        return path.substring(path.lastIndexOf(PATH_SEPARATOR) + 1);
    }
}

