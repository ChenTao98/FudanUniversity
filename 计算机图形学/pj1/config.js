//画布的大小
var canvasSize = {"maxX": 1024, "maxY": 768};
var circleR = 10;
var colorRed = [255, 0, 0];
//数组中每个元素表示一个点的坐标[x,y,z]，这里一共有9个点
var vertex_pos = [
    [0, 0, 0],
    [700, 0, 0],
    [1000, 0, 0],
    [100, 400, 0],
    [600, 450, 0],
    [1000, 400, 0],
    [50, 650, 0],
    [700, 700, 0],
    [1000, 700, 0]
];

//顶点颜色数组，保存了上面顶点数组中每个顶点颜色信息[r,g,b]
var vertex_color = [
    [0, 0, 255],
    [0, 255, 0],
    [0, 255, 255],
    [255, 255, 0],
    [0, 255, 255],
    [0, 255, 0],
    [0, 255, 0],
    [0, 200, 100],
    [255, 255, 0]
];

//四边形数组，数组中每个元素表示一个四边形，其中的四个数字是四边形四个顶点的index，例如vertex[polygon[2][1]]表示第三个多边形的第2个顶点的坐标
var polygon = [
    [0, 1, 4, 3],
    [1, 2, 5, 4],
    [3, 4, 7, 6],
    [4, 5, 8, 7]
];

//该函数在一个canvas上绘制一个点
//其中cxt是从canvas中获得的一个2d上下文context
//    x,y分别是该点的横纵坐标
//    color是表示颜色的整形数组，形如[r,g,b]
//    color在这里会本转化为表示颜色的字符串，其内容也可以是：
//        直接用颜色名称:   "red" "green" "blue"
//        十六进制颜色值:   "#EEEEFF"
//        rgb分量表示形式:  "rgb(0-255,0-255,0-255)"
//        rgba分量表示形式:  "rgba(0-255,1-255,1-255,透明度)"
//由于canvas本身没有绘制单个point的接口，所以我们通过绘制一条短路径替代
function drawPoint(cxt, x, y, color) {
    //建立一条新的路径
    cxt.beginPath();
    //设置画笔的颜色
    cxt.strokeStyle = "rgb(" + color[0] + "," +
        +color[1] + "," +
        +color[2] + ")";
    //设置路径起始位置
    cxt.moveTo(x, y);
    //在路径中添加一个节点
    cxt.lineTo(x + 1, y + 1);
    //用画笔颜色绘制路径
    cxt.stroke();
}

//绘制线段的函数绘制一条从(x1,y1)到(x2,y2)的线段，cxt和color两个参数意义与绘制点的函数相同，
function drawLine(cxt, x1, y1, x2, y2, color) {
    cxt.beginPath();
    cxt.strokeStyle = "rgba(" + color[0] + "," +
        +color[1] + "," +
        +color[2] + "," +
        +255 + ")";
    //这里线宽取1会有色差，但是类似半透明的效果有利于debug，取2效果较好
    cxt.lineWidth = 1;
    cxt.moveTo(x1, y1);
    cxt.lineTo(x2, y2);
    cxt.stroke();
}

//根据传入的线段两个端点计算线段的斜率
function calculateKAndB(pointOne, pointTwo) {
    if (pointOne[0] == pointTwo[0]) {
        //两点X坐标相同，不存在斜率
        return null;
    } else {
        let k = (pointOne[1] - pointTwo[1]) / (pointOne[0] - pointTwo[0]);
        let b = pointOne[1] - k * pointOne[0];
        return [k, b];
    }
}

//该函数用于寻找扫描线与线段的交点坐标
//输入参数： y :扫描线y的值     pointOne，pointTwo：线段两个端点     kb: 线段 y=kx+b 的斜率和b的数组； color，填充的颜色
//返回值： 如果没有交点返回 -1，有交点返回交点X坐标，如果扫描线与线段重合，直接画出线段，并返回-1；
function findNode(y, pointOne, pointTwo, kb, color) {
    //判断两个端点是否在扫描线同一侧，同侧无交点，不同侧有交点
    if (Math.max(pointOne[1], pointTwo[1]) >= y && Math.min(pointOne[1], pointTwo[1]) <= y) {
        //同侧，有交点，判断线段类型
        if (kb == null) {
            //线段没有斜率，直接返回x坐标
            return pointOne[0];
        } else if (kb[0] == 0) {
            // 线段斜率为0，与扫描线重合，直接画线，并返回-1
            drawLine(cxt, pointOne[0], y, pointTwo[0], y, color);
            return -1;
        } else {
            //正常倾斜线段，求出交点X坐标
            return [Math.round((y - kb[1]) / kb[0])];
        }
    }
    return -1;
}

//使用array.sort函数的compare接口
function sortNumber(a, b) {
    return a - b
}

//画四边形的主体函数
//传入参数：cxt：canvas的2d画板，pointArray：四边形顶点数组，存有四个顶点。
//该函数主要逻辑：获取填充颜色与顶点，计算四条边的斜率，进行扫描，根据扫描结果进行填充
function drawQuad(cxt, pointArray) {
    //获取该四边形的填充颜色
    let color = vertex_color[pointArray[0]];
    //获取四个顶点
    let pointOne = vertex_pos[pointArray[0]];
    let pointTwo = vertex_pos[pointArray[1]];
    let pointThree = vertex_pos[pointArray[2]];
    let pointFour = vertex_pos[pointArray[3]];
    let pointList = [pointOne, pointTwo, pointThree, pointFour];
    //计算四条线段的斜率
    let kbOne = calculateKAndB(pointOne, pointTwo);
    let kbTwo = calculateKAndB(pointTwo, pointThree);
    let kbThree = calculateKAndB(pointThree, pointFour);
    let kbFour = calculateKAndB(pointOne, pointFour);
    let kbList = [kbOne, kbTwo, kbThree, kbFour];
    //获取扫描范围，即四个顶点中 y 的最大值和最小值
    let maxY = Math.max(pointOne[1], pointTwo[1], pointThree[1], pointFour[1]);
    let minY = Math.min(pointOne[1], pointTwo[1], pointThree[1], pointFour[1]);
    //存储交点的数组，three用于存储本次扫描的交点，two用于存储上次扫描的交点，one用于存储上上次扫描的交点
    let nodeArrayOne = [];
    let nodeArrayTwo = [];
    let nodeArrayThree = [];
    //扫描与填充主体部分：
    //该部分主要逻辑：进行扫描，获取扫描线与四条边的全部交点存入数组
    //之后对上一次扫描的结果进行画线填充（上次的扫描结果可以根据其前后的扫描进行判断填充）
    //填充完毕，更新变量
    for (let i = minY; i <= maxY + 1; i++) {
        let count = 0;
        nodeArrayThree = [];
        //进行扫描，获取交点，存入数组
        for (let j = 0; j < 4; j++) {
            let temp = findNode(i, pointList[j], pointList[(j + 1) % 4], kbList[j], color);
            if (temp != -1) {
                nodeArrayThree[count] = temp;
                count++;
            }
        }
        //上次扫描结果进行排序
        nodeArrayTwo.sort(sortNumber);
        //根据上次的扫描结果交点个数填充
        switch (nodeArrayTwo.length) {
            //交点为两个，直接画线
            case 2:
                drawLine(cxt, nodeArrayTwo[0], i, nodeArrayTwo[1], i, color);
                break;
            //对于四边形，交点三个时，必有两个为同一点，直接画线
            case 3:
                drawLine(cxt, nodeArrayTwo[0], i, nodeArrayTwo[2], i, color);
                break;
            //交点为四个，根据前后交点结果判定
            case 4:
                if (nodeArrayThree.length == 2 && nodeArrayOne.length == 2) {
                    //前后结果交点个数均为2，则为正常四边形，直接画线
                    drawLine(cxt, nodeArrayTwo[0], i, nodeArrayTwo[3], i, color);
                } else {
                    //前后结果交点个数不为2，说明四边形有相交边，画线1和2，3和4
                    drawLine(cxt, nodeArrayTwo[0], i, nodeArrayTwo[1], i, color);
                    drawLine(cxt, nodeArrayTwo[2], i, nodeArrayTwo[3], i, color);
                }
                break;
        }
        nodeArrayOne = nodeArrayTwo;
        nodeArrayTwo = nodeArrayThree;
    }
}

//画所有的四边形，
//传入参数pointNumber为本次点击移动的点，初始时为第0个点
//该函数调用drawQuad，每次画一个四边形
// 传入pointNumber是为了让有顶点移动的四边形最后填充，由此可以覆盖其他四边形的填充
function drawQuadAll(cxt, pointNumber) {
    //存储有顶点参与移动的四边形
    let polygonMove = [];
    let countMove = 0;
    //存储没有顶点参与移动的四边形
    let polygonNotMove = [];
    let countNotMove = 0;
    //遍历四边形的顶点，判断该四边形是否有顶点移动
    for (let i = 0; i < 4; i++) {
        let isMove = false;
        for (let j = 0; j < 4; j++) {
            if (polygon[i][j] == pointNumber) {
                isMove = true;
                break;
            }
        }
        if (isMove) {
            polygonMove[countMove] = polygon[i];
            countMove++;
        } else {
            polygonNotMove[countNotMove] = polygon[i];
            countNotMove++;
        }
    }
    //先填充没有顶点移动的四边形
    for (let i = 0; i < countNotMove; i++) {
        drawQuad(cxt, polygonNotMove[i]);
    }
    //再填充有顶点移动的四边形
    for (let i = 0; i < countMove; i++) {
        drawQuad(cxt, polygonMove[i])
    }
}

// 中点画圆法
function circle(cxt, x, y, r, color) {
    let tx = 0, ty = r, d = 1 - r;
    while (tx <= ty) {
        // 利用圆的八分对称性画线
        drawLine(cxt, x + tx, y + ty, x + tx, y - ty, color);
        drawLine(cxt, x - tx, y + ty, x - tx, y - ty, color);
        drawLine(cxt, x + ty, y + tx, x + ty, y - tx, color);
        drawLine(cxt, x - ty, y + tx, x - ty, y - tx, color);
        if (d < 0) {
            d += 2 * tx + 3;
        } else {
            d += 2 * (tx - ty) + 5;
            ty--;
        }
        tx++;
    }
}

//遍历顶点数组，为每一个定点增加圆形手柄
function drawCircle(cxt) {
    for (let i = 0; i < 9; i++) {
        circle(cxt, vertex_pos[i][0], vertex_pos[i][1], circleR, colorRed);
    }
}

//判断鼠标的位置是否在canvas之内
function isInCanvas(mousex, mousey) {
    return 0 < mousex && mousex < canvasSize.maxX && 0 < mousey && mousey < canvasSize.maxY;
}

//根据鼠标的位置，判断是否在圆形手柄之内，如果在，返回手柄的下标
function getPoint(mousex, mousey) {
    for (let i = 0; i < 9; i++) {
        let a2 = Math.pow(mousex - vertex_pos[i][0], 2);
        let b2 = Math.pow(mousey - vertex_pos[i][1], 2);
        if (Math.sqrt(a2 + b2) < circleR) {
            return i;
        }
    }
    return -1;
}

var pointNum = -1;

//鼠标按下事件
function doMousedown(ev) {
    let mouseEvent = window.event || ev;
    let tempX = mouseEvent.offsetX;
    let tempY = mouseEvent.offsetY;
    // 如果按下位置在canvas内，且在一个手柄呢，启动鼠标移动事件
    pointNum = getPoint(tempX, tempY);
    if (pointNum != -1 && isInCanvas(tempX, tempY)) {
        document.addEventListener("mousemove", doMousemove);
    }
}

//鼠标移动事件
function doMousemove(ev) {
    let mouseEvent = window.event || ev;
    let tempX = mouseEvent.offsetX;
    let tempY = mouseEvent.offsetY;
    //如果鼠标在canvas内，更新画板
    if (isInCanvas(tempX, tempY)) {
        vertex_pos[pointNum][0] = tempX;
        vertex_pos[pointNum][1] = tempY;
        cxt.clearRect(0, 0, c.width, c.height);
        drawQuadAll(cxt, pointNum);
        drawCircle(cxt);
    }
}

//鼠标松开事件，解除鼠标移动事件
function doMouseup(ev) {
    doMousemove(ev);
    document.removeEventListener("mousemove", doMousemove);
}

var c = document.getElementById("myCanvas");
//设置canvas的宽和高
c.setAttribute("width", canvasSize.maxX);
c.setAttribute("height", canvasSize.maxY);
var cxt = c.getContext("2d");
cxt.translate(0.5, 0.5);
//初始画四边形和圆形手柄
drawQuadAll(cxt, 0);
drawCircle(cxt);
//绑定鼠标按下和松开事件
document.addEventListener("mousedown", doMousedown);
document.addEventListener("mouseup", doMouseup);