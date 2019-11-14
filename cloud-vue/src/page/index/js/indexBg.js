let interval;

export function startCanvas(){
  var canvasElement=document.getElementById('index_bg_canvas');
  var cxt=canvasElement.getContext("2d");

  var windowW=window.innerWidth;
  var windowh=window.innerHeight;
  canvasElement.width=windowW;
  canvasElement.height=windowh;
  //画星星
  var nums=200;
  var starArr=[];
  for(var i=0;i<nums;i++){
    var arrs=["t","f"];
    var r= Math.random()*4;  //生成星星的半径
    var ax=Math.random()*canvasElement.width;
    var ay=Math.random()*canvasElement.height;
    var opactityS=Math.random()*1;
    opactityS=opactityS>0.7?0.7:opactityS;
    var trues=arrs[Math.floor(Math.random()*2)];

    starArr.push({
      x:ax,
      y:ay,
      R:r,
      initX:ax,
      initY:ay,
      moves:trues,
      initOpacity:opactityS,
      opacity:opactityS
    });
  }

  //流线
  var newLine=true;
  var linesArr=[];
  var conter=0;
  function createLine(){
    if(newLine==true){
      var lineNum=Math.ceil(Math.random()*2);
      linesArr=[]
      for(var i=0;i<lineNum;i++){
        var lx=Math.random()*canvasElement.width;
        var ly=Math.random()*canvasElement.height-Math.random()*500;
        linesArr.push({
          initX:lx,
          initY:0,
          length:Math.random()*50+50,
          speed:Math.random()*10
        })
      }
      newLine=false;
    }
  }

  function draw(){
    cxt.clearRect(0,0,canvasElement.width,canvasElement.height);
    var colors=cxt.createLinearGradient(0,0,0,canvasElement.height);
    cxt.fillStyle=colors;
    cxt.fillRect(0,0,canvasElement.width,canvasElement.height);

    for(var i=0;i<starArr.length;i++){
      var child=starArr[i];
      cxt.save();
      cxt.globalAlpha=child.opacity;
      cxt.beginPath();
      cxt.fillStyle="#2564ad";
      cxt.arc(child.initX,child.initY,child.R,0,Math.PI*2,false);
      cxt.fill();
      cxt.closePath();
      //移动
      if(child.moves=='t'){
        child.initX+=Math.random()*1*0.5;  //星星移动的速度
        if(child.initX<0){
          child.initX=child.x;
        }
      }
      //透明度
      if(child.opacity<0.8){
        child.opacity+=0.005;
        if(child.opacity>0.7){
          child.opacity=child.initOpacity;
        }
      }
      cxt.restore();
    }

    //lines
    createLine();

    for(var i=0;i<linesArr.length;i++){
      var child=linesArr[i];
      cxt.save();
      cxt.rotate(0);
      cxt.beginPath();
      cxt.strokeStyle="#3157ab";
      cxt.moveTo(child.initX,child.initY);
      cxt.lineTo(child.initX-Math.cos(Math.PI/180*147)*child.length,child.initY+Math.sin(Math.PI/180*147)*child.length);
      cxt.stroke();
      cxt.restore();
      child.speed+=0.1;
      var preX=Math.cos(Math.PI/180*147)*child.speed;
      var preY=Math.sin(Math.PI/180*147)*child.speed;
      child.initX=child.initX-preX;
      child.initY=child.initY+preY;
      if(child.initX<0||child.initY>canvasElement.height){
        conter=i+1;
      }
      if(conter==linesArr.length){
        conter=0;
        newLine=true;
      }
    }
  }

  draw();

  interval = setInterval(draw,20);
}

export function clearCanvas() {
  clearInterval(interval);
}
