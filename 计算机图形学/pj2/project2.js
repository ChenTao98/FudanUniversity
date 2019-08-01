var VSHADER_SOURCE =
    'attribute vec4 a_Position;\n' +
    'attribute vec4 a_Color;\n' +
    'varying vec4 v_Color;\n' +
    'uniform mat4 u_ModelMatrix;\n' +
    'void main() {\n' +
    '  gl_Position = u_ModelMatrix * a_Position;\n' +
    '  v_Color = a_Color;\n' +
    '}\n';

// Fragment shader program
var FSHADER_SOURCE =
    'precision mediump float;\n' +
    'varying vec4 v_Color;\n' +
    'void main() {\n' +
    '  gl_FragColor = v_Color;\n' +
    '}\n';


function main() {
    //获取canvas对象，并且设置长宽
    let canvas = document.getElementById('webgl');
    canvas.setAttribute("width", canvasSize.maxX);
    canvas.setAttribute("height", canvasSize.maxY);
    //对调矩形的第一和第二个顶点，这样使用drawTriangleStrip时可以正确绘图
    for (let i = 0; i < 4; i++) {
        let temp = polygon[i][0];
        polygon[i][0] = polygon[i][1];
        polygon[i][1] = temp;
    }
    // Get the rendering context for WebGL
    let gl = getWebGLContext(canvas);
    if (!gl) {
        console.log('Failed to get the rendering context for WebGL');
        return;
    }

    // Initialize shaders
    if (!initShaders(gl, VSHADER_SOURCE, FSHADER_SOURCE)) {
        console.log('Failed to intialize shaders.');
        return;
    }
    let n = initVertexBuffers(gl);
    if (n < 0) {
        console.log('Failed to set the vertex information');
        return;
    }
    // Specify the color for clearing <canvas>
    gl.clearColor(0.0, 0.0, 0.0, 1.0);
    //获取变换矩阵
    let u_ModelMatrix = gl.getUniformLocation(gl.program, 'u_ModelMatrix');
    if (!u_ModelMatrix) {
        console.log('Failed to get the storage location of u_ModelMatrix');
        return;
    }
    //设置键盘按下事件
    document.onkeydown = function (ev) {
        keydown(ev, gl, u_ModelMatrix);
    };
    //设置鼠标按下事件
    document.onmousedown = function (ev) {
        doMousedown(ev, gl, u_ModelMatrix);
    };
    //设置鼠标松开事件
    document.onmouseup = function (ev) {
        doMouseup(ev, gl, u_ModelMatrix);
    };
    // initDraw(gl, u_ModelMatrix);
    draw(gl, currentAngle, currentScale, u_ModelMatrix);
}

let verticesColors = new Float32Array(4);//顶点颜色数组
const numOfQuad = 4;//四边形个数
const numOfQuadVer = 4;//四边形顶点个数
const halfCanvasWidth = canvasSize.maxX / 2;//事先计算好面板的宽度半值
const halfCanvasHeight = canvasSize.maxY / 2;//面板高度半值

//初始化顶点buffer的函数
function initVertexBuffers(gl) {
    let arrayVerticesColors = [];//存储顶点以及颜色信息
    let n = 4;
    let temp = 0;
    //遍历四边形各个顶点，存储他们的位置以及颜色信息
    for (let i = 0; i < numOfQuad; i++) {
        let quad = polygon[i];
        for (let j = 0; j < numOfQuadVer; j++) {
            let point = quad[j];
            arrayVerticesColors[temp++] = (vertex_pos[point][0] - halfCanvasWidth) / halfCanvasWidth;
            arrayVerticesColors[temp++] = (halfCanvasHeight - vertex_pos[point][1]) / halfCanvasHeight;
            arrayVerticesColors[temp++] = vertex_color[point][0] / 255;
            arrayVerticesColors[temp++] = vertex_color[point][1] / 255;
            arrayVerticesColors[temp++] = vertex_color[point][2] / 255;
        }
    }
    //再次存储顶点信息，用于绘制网格
    for (let i = 0; i < numOfQuad; i++) {
        let quad = polygon[i];
        for (let j = 0; j < numOfQuadVer; j++) {
            let point = quad[j];
            arrayVerticesColors[temp++] = (vertex_pos[point][0] - halfCanvasWidth) / halfCanvasWidth;
            arrayVerticesColors[temp++] = (halfCanvasHeight - vertex_pos[point][1]) / halfCanvasHeight;
            arrayVerticesColors[temp++] = 1.0;
            arrayVerticesColors[temp++] = 0.0;
            arrayVerticesColors[temp++] = 0.0;
        }
    }
    //将顶点的信息存储为Float32Array
    verticesColors = new Float32Array(arrayVerticesColors);

    // Create a buffer object
    let vertexColorBuffer = gl.createBuffer();
    if (!vertexColorBuffer) {
        console.log('Failed to create the buffer object');
        return false;
    }

    // Bind the buffer object to target
    gl.bindBuffer(gl.ARRAY_BUFFER, vertexColorBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, verticesColors, gl.DYNAMIC_DRAW);

    let FSIZE = verticesColors.BYTES_PER_ELEMENT;
    //Get the storage location of a_Position, assign and enable buffer
    let a_Position = gl.getAttribLocation(gl.program, 'a_Position');
    if (a_Position < 0) {
        console.log('Failed to get the storage location of a_Position');
        return -1;
    }
    gl.vertexAttribPointer(a_Position, 2, gl.FLOAT, false, FSIZE * 5, 0);
    gl.enableVertexAttribArray(a_Position);  // Enable the assignment of the buffer object\
    // Get the storage location of a_Position, assign buffer and enable
    let a_Color = gl.getAttribLocation(gl.program, 'a_Color');
    if (a_Color < 0) {
        console.log('Failed to get the storage location of a_Color');
        return -1;
    }
    gl.vertexAttribPointer(a_Color, 3, gl.FLOAT, false, FSIZE * 5, FSIZE * 2);
    gl.enableVertexAttribArray(a_Color);  // Enable the assignment of the buffer object
    // Unbind the buffer object
    // gl.bindBuffer(gl.ARRAY_BUFFER, null);
    return n;
}

let KEY_T = 84;//T 键对应数值
let KEY_B = 66;//B 键对应数值
let KEY_E = 69;//E 键对应数值
let isRotating = false;//当前是否在旋转的布尔值
let isNeedShowLine = true;//当前是否需要绘制边框的布尔值
let canModify = true;//但是是否是编辑状态的布尔值

//设置键盘响应事件函数
function keydown(ev, gl, u_ModelMatrix) {
    switch (ev.keyCode) {
        case KEY_T:
            if (isRotating) {
                stopRotate();//旋转状态下，停止旋转
            } else {
                startRotate(gl, u_ModelMatrix);//非旋转状态下，开始旋转
            }
            break;
        case KEY_B:
            isNeedShowLine = !isNeedShowLine;//将是否需要展示边框布尔值取反
            //重新绘制图形
            draw(gl, currentAngle, currentScale, u_ModelMatrix);
            break;
        case KEY_E:
            //如果旋转状态下，就停止旋转
            if (isRotating) {
                stopRotate();
            }
            //修改编辑状态，重新设置角度与比例
            canModify = true;
            currentAngle = 0;
            currentScale = 1;
            //重新绘制图形
            draw(gl, currentAngle, currentScale, u_ModelMatrix);
            break;
    }
}

let currentAngle = 0.0;//当前旋转角度
let currentScale = 1.0;//当前缩放比例
let animationID = null;//回调函数id

//开始旋转函数
function startRotate(gl, u_ModelMatrix) {
    // Start drawing
    g_last = Date.now();
    //设置回调函数，每一帧都会调用
    let tick = function () {
        animate();  // 更新角度与比例函数
        draw(gl, currentAngle, currentScale, u_ModelMatrix);   // Draw the triangle
        animationID = requestAnimationFrame(tick); // Request that the browser ?calls tick
    };
    tick();
    isRotating = true;//设置旋转状态
    canModify = false;//设置不可编辑
}

//停止旋转函数
function stopRotate() {
    window.cancelAnimationFrame(animationID);//取消每帧的刷新事件
    isRotating = false;//设置非旋转状态
}

// Last time that this function was called
let g_last = Date.now();//设置时间
let isLarger = false;//但是缩放比例是否在增大的布尔值
const ANGLE_STEP = 45.0;//每秒旋转角度
const SCALE_STEP = 0.2;//每秒缩放的比例

//更新旋转角度与缩放比例函数
function animate() {
    // Calculate the elapsed time
    let now = Date.now();
    let elapsed = now - g_last;
    g_last = now;
    // 更新当前角度
    currentAngle = (currentAngle + (ANGLE_STEP * elapsed) / 1000.0) % 360;
    //计算缩放变化比例
    let scale = SCALE_STEP * elapsed / 1000;
    //根据当前是在缩放还是减小来更新比例
    if (isLarger) {
        if (currentScale + scale < 1) {
            currentScale += scale;
        } else {
            currentScale = 2 - currentScale - scale;
            isLarger = false;
        }
    } else {
        if (currentScale - scale > 0.2) {
            currentScale -= scale;
        } else {
            currentScale = 0.4 - currentScale + scale;
            isLarger = true;
        }
    }
}

let modelMatrix = new Matrix4();//变换矩阵

//绘制图形函数
function draw(gl, currentAngle, scale, u_ModelMatrix) {
    // 设置旋转缩放矩阵
    modelMatrix.setRotate(currentAngle, 0, 0, 1);
    modelMatrix.scale(scale, scale, scale);
    // Pass the rotation matrix to the vertex shader
    gl.uniformMatrix4fv(u_ModelMatrix, false, modelMatrix.elements);
    // 清除当前界面
    gl.clear(gl.COLOR_BUFFER_BIT);
    //重新绘制
    for (let i = 0; i < 4; i++) {
        gl.drawArrays(gl.TRIANGLE_STRIP, i * 4, 4);
    }
    //判定是否需要绘制边框
    if (isNeedShowLine) {
        for (let i = 0; i < 4; i++) {
            gl.drawArrays(gl.LINE_LOOP, 16 + i * 4, 3);
            gl.drawArrays(gl.LINE_LOOP, 17 + i * 4, 3);
        }
    }
}

const circleR = 10;//手柄半径

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

let pointNum = -1;//当前点击的点下标

//鼠标按下事件
function doMousedown(ev, gl, u_ModelMatrix) {
    //如果是可编辑状态进行判定位置信息
    if (canModify) {
        let mouseEvent = window.event || ev;
        let tempX = mouseEvent.offsetX;
        let tempY = mouseEvent.offsetY;
        // 如果按下位置在canvas内，且在一个手柄内，启动鼠标移动事件
        pointNum = getPoint(tempX, tempY);
        if (pointNum !== -1 && isInCanvas(tempX, tempY)) {
            document.onmousemove = function (ev) {
                doMousemove(ev, gl, u_ModelMatrix);
            }
        }
    }
}

//判断鼠标的位置是否在canvas之内
function isInCanvas(mousex, mousey) {
    return 0 < mousex && mousex < canvasSize.maxX && 0 < mousey && mousey < canvasSize.maxY;
}

//鼠标移动事件
function doMousemove(ev, gl, u_ModelMatrix) {
    let mouseEvent = window.event || ev;
    let tempX = mouseEvent.offsetX;
    let tempY = mouseEvent.offsetY;
    //如果鼠标在canvas内，更新画板
    if (isInCanvas(tempX, tempY) && pointNum !== -1) {
        vertex_pos[pointNum][0] = tempX;
        vertex_pos[pointNum][1] = tempY;
        //直接重新构建顶点颜色数组
        // initVertexBuffers(gl);
        // draw(gl, currentAngle, currentScale, u_ModelMatrix);
        //更新buffer的顶点坐标
        // for (let i = 0; i < numOfQuad; i++) {
        //     let quad = polygon[i];
        //     let tempIndex = i * numOfQuadVer * 5;
        //     for (let j = 0; j < numOfQuadVer; j++) {
        //         if (quad[j] === pointNum) {
        //             verticesColors[tempIndex + j * 5] = (vertex_pos[pointNum][0] - halfCanvasWidth) / halfCanvasWidth;
        //             verticesColors[tempIndex + j * 5 + 1] = (halfCanvasHeight - vertex_pos[pointNum][1]) / halfCanvasHeight;
        //             verticesColors[numOfQuad * numOfQuadVer * 5 + tempIndex + j * 5] = (vertex_pos[pointNum][0] - halfCanvasWidth) / halfCanvasWidth;
        //             verticesColors[numOfQuad * numOfQuadVer * 5 + tempIndex + j * 5 + 1] = (halfCanvasHeight - vertex_pos[pointNum][1]) / halfCanvasHeight;
        //         }
        //     }
        // }
        // //重新绑定坐标
        // gl.bufferData(gl.ARRAY_BUFFER, verticesColors, gl.DYNAMIC_DRAW);
        let a_Position = gl.getAttribLocation(gl.program, 'a_Position');
        let size=Float32Array.BYTES_PER_ELEMENT;
		//更新buffer的顶点坐标
        for (let i = 0; i < numOfQuad; i++) {
            let quad = polygon[i];
            let tempIndex = i * numOfQuadVer * 5;
            for (let j = 0; j < numOfQuadVer; j++) {
                if (quad[j] === pointNum) {
                    let tempBuff=new Float32Array([(vertex_pos[pointNum][0] - halfCanvasWidth) / halfCanvasWidth,
                        (halfCanvasHeight - vertex_pos[pointNum][1]) / halfCanvasHeight]);
                    gl.bufferSubData(gl.ARRAY_BUFFER,(tempIndex+j*5)*size,tempBuff);
                    gl.bufferSubData(gl.ARRAY_BUFFER,(numOfQuad * numOfQuadVer * 5 + tempIndex + j * 5)*size,tempBuff);
                }
            }
        }
        //重新绑定坐标
        // gl.bufferData(gl.ARRAY_BUFFER, verticesColors, gl.DYNAMIC_DRAW);
        //绘制图形
        draw(gl, currentAngle, currentScale, u_ModelMatrix);
    }
}

//鼠标松开事件，解除鼠标移动事件
function doMouseup(ev, gl, u_ModelMatrix) {
    if (canModify) {
        doMousemove(ev, gl, u_ModelMatrix);
        document.onmousemove = null;
    }
}