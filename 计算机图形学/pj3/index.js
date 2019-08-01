//载入obj的顶点着色器
let VSHADER_LIGHT2_SOURCE =
    'attribute vec4 a_Position;\n' +
    'attribute vec4 a_Color;\n' +
    'attribute vec4 a_Normal;\n' +
    'uniform mat4 u_MvpMatrix;\n' +
    'uniform mat4 u_NormalMatrix;\n' +
    'uniform vec4 u_Eye;\n' +
    'uniform mat4 u_ModelMatrix;\n' +   // Model matrix
    'varying vec4 v_Color;\n' +
    'varying float v_Dist;\n' +
    'varying vec4 v_Position;\n' +
    'varying vec3 v_Normal;\n' +
    'void main() {\n' +
    '  gl_Position = u_MvpMatrix * a_Position;\n' +
    '  v_Normal = normalize(vec3(u_NormalMatrix * a_Normal));\n' +
    '  v_Position = u_ModelMatrix * a_Position;\n' +
    '  v_Color = a_Color;\n' +
    '  v_Dist = distance(v_Position,u_Eye);\n' +
    '}\n';

//载入obj的片元着色器
let FSHADER_LIGHT2_SOURCE =
    '#ifdef GL_ES\n' +
    'precision mediump float;\n' +
    '#endif\n' +
    'uniform vec3 u_FogColor;\n' +     //雾化颜色
    'uniform vec2 u_FogDist;\n' +     //雾化距离
    'uniform vec3 u_LightColor;\n' +   //点光源颜色
    'uniform vec3 u_LightPosition;\n' + // 点光源位置
    'uniform vec3 u_DirectionLight;\n' +  //平行光方向
    'uniform vec3 u_AmbientLight;\n' +   //环境光颜色

    'varying vec4 v_Color;\n' +
    'varying vec3 v_Normal;\n' +
    'varying vec4 v_Position;\n' +
    'varying float v_Dist;\n' +

    'void main() {\n' +
    '  vec3 normal = normalize(v_Normal);\n' +
    //计算方向光
    '  float nDotLightDirection = max(dot(normal, u_DirectionLight), 0.0);\n' +
    '  vec3 diffuse = v_Color.rgb * nDotLightDirection;\n' +
    //计算点光源
    '  vec3 lightPointDirection = normalize(u_LightPosition - vec3(v_Position));\n' +
    '  float nDotLightPoint = max(dot(normal, lightPointDirection), 0.0);\n' +
    '  vec3 point = u_LightColor * v_Color.rgb * nDotLightPoint;\n' +
    //计算环境光
    '  vec3 ambient = u_AmbientLight * v_Color.rgb;\n' +
    '  vec4 tempColor = vec4(ambient+diffuse+point , v_Color.a);\n' +

    '  float fogFactor = clamp((u_FogDist.y - v_Dist)/(u_FogDist.y , u_FogDist.x),0.0,1.0);\n' +
    '  vec3 color = mix(u_FogColor,vec3(tempColor),fogFactor);\n' +
    '  gl_FragColor =vec4(color,tempColor.a);\n' +
    '}\n';
//载入纹理的顶点着色器
let TEXTURE_VSHADER_SOURCE =
    'attribute vec4 a_Position;\n' +
    'attribute vec2 a_TexCoord;\n' +
    'uniform mat4 u_MvpMatrix;\n' +
    'varying vec2 v_TexCoord;\n' +

    'uniform vec4 u_Eye;\n' +
    'varying float v_Dist;\n' +

    'attribute vec4 a_Normal;\n' +
    'uniform mat4 u_NormalMatrix;\n' +
    'uniform mat4 u_ModelMatrix;\n' +
    'uniform vec3 u_LightColor;\n' +    //点光源颜色
    'uniform vec3 u_LightPosition;\n' + // 点光源位置
    'uniform vec3 u_DirectionLight;\n' + //平行光方向
    'uniform vec3 u_AmbientLight;\n' + //环境光颜色
    'varying float nDotLightDirection;\n' +
    'varying float nDotLightPoint;\n' +
    'varying vec3 v_lightPointColor;\n' +
    'varying vec3 v_lightAmbientColor;\n' +


    'void main() {\n' +
    '  gl_Position = u_MvpMatrix * a_Position;\n' +
    '  v_TexCoord = a_TexCoord;\n' +
    '  vec3 normal = normalize(vec3(u_NormalMatrix * a_Normal));\n' +
    //计算方向光
    '  nDotLightDirection = max(dot(normal, u_DirectionLight), 0.0);\n' +
    //计算点光源
    '  vec4 vertexPosition = u_ModelMatrix * a_Position;\n' +
    '  vec3 lightPointDirection = normalize(u_LightPosition - vec3(vertexPosition));\n' +
    '  nDotLightPoint = max(dot(normal, lightPointDirection), 0.0);\n' +
    '  v_lightPointColor = u_LightColor;\n' +
    //计算环境光
    '  v_lightAmbientColor = u_AmbientLight;\n' +
    '  v_Dist = distance(vertexPosition,u_Eye);\n' +
    '}\n';

//载入纹理的片元着色器
let TEXTURE_FSHADER_SOURCE =
    '#ifdef GL_ES\n' +
    'precision mediump float;\n' +
    '#endif\n' +
    'uniform sampler2D u_Sampler;\n' +
    'varying vec2 v_TexCoord;\n' +
    //光源参数
    'varying float nDotLightDirection;\n' +
    'varying float nDotLightPoint;\n' +
    'varying vec3 v_lightPointColor;\n' +
    'varying vec3 v_lightAmbientColor;\n' +
    //雾化参数
    'uniform vec3 u_FogColor;\n' +
    'uniform vec2 u_FogDist;\n' +
    'varying float v_Dist;\n' +

    'void main() {\n' +
    'vec4 color=texture2D(u_Sampler, v_TexCoord);\n' +
    'vec3 diffuse = color.rgb * nDotLightDirection;\n' + //计算方向光
    'vec3 point = v_lightPointColor * color.rgb * nDotLightPoint;\n' + //计算点光源
    'vec3 ambient = v_lightAmbientColor * color.rgb;\n' + //计算环境光
    'vec4 tempColor = vec4(diffuse+point+ambient,color.a);\n' +
    'float fogFactor = clamp((u_FogDist.y - v_Dist)/(u_FogDist.y , u_FogDist.x),0.0,1.0);\n' +
    'vec3 mColor = mix(u_FogColor,vec3(tempColor),fogFactor);\n' +
    'gl_FragColor =vec4(mColor,tempColor.a);\n' +
    '}\n';

let eye = new Vector3(CameraPara.eye);//眼睛所在位置
let at = new Vector3(CameraPara.at);//look at位置
let up = new Vector3(CameraPara.up);//up向量

let fogDist = new Float32Array([100, 150]); //雾化时距离
let fogDistFloor = new Float32Array([300,300]); //雾化时地板距离，地板比较特殊，距离需要单独
let fogColor = new Float32Array([0.92, 0.92, 0.92]); //雾化颜色

let SceneObjectList = []; //存放obj对象的数组
let keyDownArray = []; //存放按下的键的数组
let gl;
let canvas;
let dbgmsg; // 输出点的信息
let SceneObject = function () {
    this.model;  	 //a model contains some vertex buffer
    this.filePath;   //obj file path
    this.objDoc;
    this.drawingInfo;
    this.transform;
    this.valid = 0;
};

//main函数
function main() {
    //初始化html元素
    canvas = document.getElementById('webgl');
    dbgmsg = document.getElementById("messageBox");
    gl = getWebGLContext(canvas);
    if (!gl) {
        console.log('Failed to get the rendering context for WebGL');
        return;
    }
    //初始化obj的shader和texture的shader
    light2Program = initObjectProgram(gl);
    let texProgram = initTextureProgram(gl);
    if (!light2Program || !texProgram) {
        console.log('Failed to intialize shaders.');
        return;
    }
    //获取视角矩阵
    let mvpMatrix = setViewMatrix();
    //初始化obj的数组，将obj的数据读取保存
    initObjectList();
    //初始化地板和箱子的对象，获取其坐标等信息
    let floorObject = initTextureVertexBuffers(gl, floorRes.vertex, floorRes.texCoord, floorRes.index, normalFloor);
    let cubeObject = initTextureVertexBuffers(gl, boxRes.vertex, boxRes.texCoord, boxRes.index, normalCube);
    if (!floorObject || !cubeObject) {
        console.log('Failed to get the object');
        return;
    }
    //初始化地板和箱子纹理，将其与图片绑定
    let textureFloor = initTextureBuffers(gl, texProgram, floorRes.texImagePath);
    let textureCube = initTextureBuffers(gl, texProgram, boxRes.texImagePath);
    if (!textureCube || !textureFloor) {
        console.log("init textureBuffer fail");
        return;
    }
    //tick函数，每隔一段时间自动重画
    let tick = function () {
        //启用隐藏面消除，清除画板
        gl.enable(gl.DEPTH_TEST);
        gl.clearColor(fogColor[0], fogColor[1], fogColor[2], 1.0);
        gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
        //设置不需要点光源
        isPointLight = false;
        //更新上次到这次间隔角度、位移变化
        updateAngle();
        //根据上一步更新的数据并根据已经按下的键盘更新相机位置等信息
        updateKey();
        //设置视角矩阵
        mvpMatrix = setViewMatrix();
        //绘制obj
        renderScene(gl, mvpMatrix);
        //绘制箱子和地板
        drawTexture(gl, texProgram, textureCube, cubeObject, mvpMatrix, boxRes, false);
        drawTexture(gl, texProgram, textureFloor, floorObject, mvpMatrix, floorRes, true);
        //更新debug信息
        showDebugMassage();
        window.requestAnimationFrame(tick, canvas);
    };
    tick();
    //设置键盘按下与松开事件
    document.onkeydown = function (ev) {
        keydown(ev);
    };
    document.onkeyup = function (ev) {
        keyup(ev);
    };
}
//初始化obj的shader，会获取相应的attribute等
function initObjectProgram(gl) {
    let light2Program = createProgram(gl, VSHADER_LIGHT2_SOURCE, FSHADER_LIGHT2_SOURCE);
    if (!light2Program) {
        return null;
    }
    light2Program.a_Position = gl.getAttribLocation(light2Program, 'a_Position');
    light2Program.a_Color = gl.getAttribLocation(light2Program, 'a_Color');
    light2Program.u_MvpMatrix = gl.getUniformLocation(light2Program, 'u_MvpMatrix');
    light2Program.a_Normal = gl.getAttribLocation(light2Program, 'a_Normal');
    light2Program.u_NormalMatrix = gl.getUniformLocation(light2Program, 'u_NormalMatrix');
    light2Program.u_ModelMatrix = gl.getUniformLocation(light2Program, 'u_ModelMatrix');
    light2Program.u_LightColor = gl.getUniformLocation(light2Program, 'u_LightColor');
    light2Program.u_LightPosition = gl.getUniformLocation(light2Program, 'u_LightPosition');
    light2Program.u_DirectionLight = gl.getUniformLocation(light2Program, 'u_DirectionLight');
    light2Program.u_AmbientLight = gl.getUniformLocation(light2Program, 'u_AmbientLight');
    light2Program.u_Eye = gl.getUniformLocation(light2Program, 'u_Eye');
    light2Program.u_FogColor = gl.getUniformLocation(light2Program, 'u_FogColor');
    light2Program.u_FogDist = gl.getUniformLocation(light2Program, 'u_FogDist');

    if (light2Program.a_Position < 0 || light2Program.a_Color < 0 || light2Program.a_Normal < 0
        || !light2Program.u_MvpMatrix || !light2Program.u_NormalMatrix || !light2Program.u_DirectionLight) {
        console.log('Failed to get the storage location of attribute or uniform variable');
        return null;
    }
    return light2Program;
}
//初始化obj的数组
//根据scene.js获取对象，读取信息
function initObjectList() {
    for (let i = 0; i < ObjectList.length; i++) {
        let e = ObjectList[i];
        let so = new SceneObject();
        //初始化模型，获取obj的颜色等信息
        so.model = initObjectVertexBuffers(gl, light2Program);
        if (!so.model) {
            console.log('Failed to set the vertex information');
            so.valid = 0;
            continue;
        }
        so.valid = 1;
        so.kads = e.kads;
        so.transform = e.transform;
        so.objFilePath = e.objFilePath;
        so.color = e.color;
        //补齐最后一个alpha值
        if (so.color.length == 3) {
            so.color.push(1.0);
        }
        //读取obj文件
        readOBJFile(so, gl, 1.0, true);
        //压入物体列表中
        SceneObjectList.push(so);
    }
}
//初始化纹理的shader，获取其属性
function initTextureProgram(gl) {
    let texProgram = createProgram(gl, TEXTURE_VSHADER_SOURCE, TEXTURE_FSHADER_SOURCE);
    if (!texProgram) {
        console.log('Failed to intialize texture shaders.');
        return null;
    }
    texProgram.a_Color = gl.getAttribLocation(texProgram, 'a_Color');
    texProgram.a_Position = gl.getAttribLocation(texProgram, 'a_Position');
    texProgram.a_TexCoord = gl.getAttribLocation(texProgram, 'a_TexCoord');
    texProgram.u_MvpMatrix = gl.getUniformLocation(texProgram, 'u_MvpMatrix');
    texProgram.u_Sampler = gl.getUniformLocation(texProgram, 'u_Sampler');
    texProgram.a_Normal = gl.getAttribLocation(texProgram, 'a_Normal');
    texProgram.u_NormalMatrix = gl.getUniformLocation(texProgram, 'u_NormalMatrix');
    texProgram.u_ModelMatrix = gl.getUniformLocation(texProgram, 'u_ModelMatrix');
    texProgram.u_LightColor = gl.getUniformLocation(texProgram, 'u_LightColor');
    texProgram.u_LightPosition = gl.getUniformLocation(texProgram, 'u_LightPosition');
    texProgram.u_DirectionLight = gl.getUniformLocation(texProgram, 'u_DirectionLight');
    texProgram.u_AmbientLight = gl.getUniformLocation(texProgram, 'u_AmbientLight');
    texProgram.u_Eye = gl.getUniformLocation(texProgram, 'u_Eye');
    texProgram.u_FogColor = gl.getUniformLocation(texProgram, 'u_FogColor');
    texProgram.u_FogDist = gl.getUniformLocation(texProgram, 'u_FogDist');
    return texProgram;
}
//设置视角矩阵，设置look at和投影矩阵，然后获取视角矩阵
function setViewMatrix() {
    let viewMatrix = new Matrix4();
    let projMatrix = new Matrix4();  // Projection matrix
    let mvpMatrix = new Matrix4();   // Model view projection matrix
    //设置look at
    viewMatrix.setLookAt(eye.elements[0], eye.elements[1], eye.elements[2],
        at.elements[0], at.elements[1], at.elements[2],
        up.elements[0], up.elements[1], up.elements[2]
    );
    //设置投影
    projMatrix.setPerspective(CameraPara.fov, canvas.width / canvas.height, CameraPara.near, CameraPara.far);
    mvpMatrix.set(projMatrix).multiply(viewMatrix);
    return mvpMatrix;
}

let obj_modelMatrix = new Matrix4(); // obj的Model Matrix
let obj_mvpMatrix = new Matrix4();   // Model view projection matrix
let obj_normalMatrix = new Matrix4(); // obj的法向量矩阵

let birdAngle = 0;//小鸟旋转的角度
let ANGLE_STEP = 60;//小鸟每秒旋转的角度
//绘制obj
function renderScene(gl, viewMatrix) {

    gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

    gl.useProgram(light2Program);
    //设置雾化的信息
    gl.uniform4fv(light2Program.u_Eye, new Float32Array([eye.elements[0], eye.elements[1], eye.elements[2], 0]));
    gl.uniform3fv(light2Program.u_FogColor, fogColor);
    gl.uniform2fv(light2Program.u_FogDist, fogDist);
    //设置光源信息，包括方向光、环境光
    gl.uniform3f(light2Program.u_DirectionLight, sceneDirectionLight[0], sceneDirectionLight[1], sceneDirectionLight[2]);
    gl.uniform3f(light2Program.u_AmbientLight, sceneAmbientLight[0], sceneAmbientLight[1], sceneAmbientLight[2]);
    //根据是否需要点光源设置点光源属性
    if (isPointLight) {
        gl.uniform3f(light2Program.u_LightColor, scenePointLightColor[0], scenePointLightColor[1], scenePointLightColor[2]);
        gl.uniform3f(light2Program.u_LightPosition, eye.elements[0], eye.elements[1], eye.elements[2]);
    } else {
        gl.uniform3f(light2Program.u_LightColor, 0, 0, 0);
        gl.uniform3f(light2Program.u_LightPosition, 0, 0, 0);
    }
    //遍历obj对象数组，画出每个obj
    for (let i = 0; i < SceneObjectList.length; i++) {
        let so = SceneObjectList[i];
        //判断读取文件是否完成，没有完成就读取文件
        if (so.objDoc != null && so.objDoc.isMTLComplete()) { // OBJ and all MTLs are available
            so.drawingInfo = onReadComplete(gl, so.model, so.objDoc);
            SceneObjectList[i].objname = so.objDoc.objects[0].name;
            so.objname = so.objDoc.objects[0].name;
            so.objDoc = null;
        }
        //判断是否有读取信息，有读取信息才绘制
        if (so.drawingInfo) {
            //设置obj的model矩阵
            obj_modelMatrix.setIdentity();
            let transform = so.transform;
            if (i !== 1) {
                //不是小鸟的obj，直接按照配置文件设置
                for (let j = 0; j < transform.length; j++) {
                    let content = transform[j].content;
                    switch (transform[j].type) {
                        case "translate":
                            obj_modelMatrix.translate(content[0], content[1], content[2]);
                            break;
                        case "rotate":
                            obj_modelMatrix.rotate(content[0], content[1], content[2], content[3]);
                            break;
                        case "scale":
                            obj_modelMatrix.scale(content[0], content[1], content[2]);
                            break;
                    }
                }
            } else {
                //如果是小鸟的话，需要动画，所以需要改变旋转、平移矩阵
                let content = transform[1].content;
                obj_modelMatrix.scale(content[0], content[1], content[2]);
                obj_modelMatrix.rotate(birdAngle * 2, 0, 1, 0);
                obj_modelMatrix.translate(1, 0.5 * Math.sin(birdAngle * angleOne) + 1, 1);
            }
            //绑定model和mvp矩阵
            gl.uniformMatrix4fv(light2Program.u_ModelMatrix, false, obj_modelMatrix.elements);
            obj_mvpMatrix.set(viewMatrix).multiply(obj_modelMatrix);
            gl.uniformMatrix4fv(light2Program.u_MvpMatrix, false, obj_mvpMatrix.elements);
            //设置法向量矩阵
            obj_normalMatrix.setInverseOf(obj_modelMatrix);
            obj_normalMatrix.transpose();
            gl.uniformMatrix4fv(light2Program.u_NormalMatrix, false, obj_normalMatrix.elements);
            //设置position等矩阵
            initAttributeVariable(gl, light2Program.a_Position, so.model.vertexBuffer);  // Vertex coordinates
            initAttributeVariable(gl, light2Program.a_Normal, so.model.normalBuffer);    // Normal
            initAttributeVariable(gl, light2Program.a_Color, so.model.colorBuffer);// Texture coordinates
            gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, so.model.indexBuffer);
            //绘制
            gl.drawElements(gl.TRIANGLES, so.drawingInfo.indices.length, gl.UNSIGNED_SHORT, 0);
        }
    }
}

let tex_mvpMatrix = new Matrix4();
let tex_modelMatrix = new Matrix4();
let tex_normalMatrix = new Matrix4();

function drawTexture(gl, texProgram, texture, object, viewMatrix, res, isFloor) {
    gl.useProgram(texProgram);//使用哪个program
    //绑定变量
    initAttributeVariable(gl, texProgram.a_Position, object.vertexBuffer);  // Vertex coordinates
    initAttributeVariable(gl, texProgram.a_TexCoord, object.texCoordBuffer);// Texture coordinates
    initAttributeVariable(gl, texProgram.a_Normal, object.normalBuffer);
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, object.indexBuffer); // Bind indices
    gl.activeTexture(gl.TEXTURE0);
    gl.bindTexture(gl.TEXTURE_2D, texture);
    //设置model矩阵
    tex_modelMatrix.setTranslate(res.translate[0], res.translate[1], res.translate[2]);
    tex_modelMatrix.scale(res.scale[0], res.scale[1], res.scale[2]);
    tex_mvpMatrix.set(viewMatrix).multiply(tex_modelMatrix);
    //设置雾化的信息
    gl.uniform4fv(texProgram.u_Eye, new Float32Array([eye.elements[0], eye.elements[1], eye.elements[2], 0]));
    gl.uniform3fv(texProgram.u_FogColor, fogColor);
    if (isFloor) {
        gl.uniform2fv(texProgram.u_FogDist, fogDistFloor);
    } else {
        gl.uniform2fv(texProgram.u_FogDist, fogDist);
    }
    //设置光源信息
    gl.uniform3f(texProgram.u_DirectionLight, sceneDirectionLight[0], sceneDirectionLight[1], sceneDirectionLight[2]);
    gl.uniform3f(texProgram.u_AmbientLight, sceneAmbientLight[0], sceneAmbientLight[1], sceneAmbientLight[2]);
    if (isPointLight) {
        gl.uniform3f(texProgram.u_LightColor, scenePointLightColor[0], scenePointLightColor[1], scenePointLightColor[2]);
        gl.uniform3f(texProgram.u_LightPosition, eye.elements[0], eye.elements[1], eye.elements[2]);
    } else {
        gl.uniform3f(texProgram.u_LightColor, 0, 0, 0);
        gl.uniform3f(texProgram.u_LightPosition, 0, 0, 0);
    }
    gl.uniformMatrix4fv(texProgram.u_ModelMatrix, false, tex_modelMatrix.elements);
    tex_normalMatrix.setInverseOf(tex_modelMatrix);
    tex_normalMatrix.transpose();
    gl.uniformMatrix4fv(texProgram.u_NormalMatrix, false, tex_normalMatrix.elements);
    gl.uniformMatrix4fv(texProgram.u_MvpMatrix, false, tex_mvpMatrix.elements);
    gl.drawElements(gl.TRIANGLES, object.numIndices, object.indexBuffer.type, 0);   // Draw
}
//键盘按下事件
//每次判断按下数组里是否有该键，没有的情况下才添加按键
function keydown(ev) {
    if (keyDownArray.indexOf(ev.keyCode) === -1) {
        keyDownArray.push(ev.keyCode);
    }
}
//键盘抬起事件
//会把对应的keyCode从数组中删除
function keyup(ev) {
    let index = keyDownArray.indexOf(ev.keyCode);
    if (index !== -1) {
        keyDownArray.splice(index, 1);
    }
}
//设置key的值
let KEY_W = 87, KEY_A = 65, KEY_S = 83, KEY_D = 68, KEY_I = 73, KEY_J = 74, KEY_K = 75, KEY_L = 76, KEY_F = 70;
let angleOne = 2 * Math.PI / 360;//1度对应的弧度
let distance = 0;//移动的距离
let angle = 0;//旋转的角度
let isPointLight = false;//是否需要点光源
//根据按键数组更新坐标
function updateKey() {
    let length = keyDownArray.length;
    //遍历数组
    for (let i = 0; i < length; i++) {
        let vectorEye = VectorMinus(at, eye).normalize();//获取视角向量
        let vectorRotateAxis;//旋转轴
        let vectorRotate;//旋转的向量
        let vectorMove;//移动的向量
        switch (keyDownArray[i]) {
            case KEY_W:
                //针对W和S，只需要根据时间间隔内的速度，在视角方向加上移动距离即可
                vectorMove = VectorMultNum(vectorEye, distance);
                eye = VectorAdd(eye, vectorMove);
                at = VectorAdd(at, vectorMove);
                break;
            case KEY_S:
                vectorMove = VectorMultNum(vectorEye, distance);
                eye = VectorMinus(eye, vectorMove);
                at = VectorMinus(at, vectorMove);
                break;
            case KEY_A:
                //针对A和D，需要获取视角和up所在的法向量，并计算在其法向量上移动的距离
                //之后才加移动的距离
                vectorMove = VectorCross(vectorEye, up).normalize();
                vectorMove = VectorMultNum(vectorMove, distance);
                eye = VectorMinus(eye, vectorMove);
                at = VectorMinus(at, vectorMove);
                break;
            case KEY_D:
                vectorMove = VectorCross(vectorEye, up).normalize();
                vectorMove = VectorMultNum(vectorMove, distance);
                eye = VectorAdd(eye, vectorMove);
                at = VectorAdd(at, vectorMove);
                break;
            case KEY_I:
                //针对I和K，需要先更新up的向量，之后根据旋转的角度，计算视角移动的距离
                //采用向量相加更新at向量
                vectorRotateAxis = VectorCross(vectorEye, up);
                up = VectorCross(vectorRotateAxis, vectorEye).normalize();
                vectorRotate = VectorCopy(up);
                vectorRotate = VectorAdd(vectorEye, VectorMultNum(vectorRotate, Math.sin(angle)));
                at = VectorAdd(eye, vectorRotate);
                break;
            case KEY_K:
                vectorRotateAxis = VectorCross(vectorEye, up);
                up = VectorCross(vectorRotateAxis, vectorEye).normalize();
                vectorRotate = VectorReverse(up);
                vectorRotate = VectorAdd(vectorEye, VectorMultNum(vectorRotate, Math.sin(angle)));
                at = VectorAdd(eye, vectorRotate);
                break;
            case KEY_J:
                //针对J和L，需要获取法向量（旋转方向），根据旋转角度计算旋转距离
                //再更新at向量
                vectorRotate = VectorCross(vectorEye, up).normalize();
                vectorRotate = VectorReverse(vectorRotate);
                vectorRotate = VectorAdd(vectorEye, VectorMultNum(vectorRotate, Math.sin(angle)));
                at = VectorAdd(eye, vectorRotate);
                break;
            case KEY_L:
                vectorRotate = VectorCross(vectorEye, up).normalize();
                vectorRotate = VectorAdd(vectorEye, VectorMultNum(vectorRotate, Math.sin(angle)));
                at = VectorAdd(eye, vectorRotate);
                break;
            case KEY_F:
                isPointLight = true;
                break;
        }
    }
}

let g_last = Date.now();//时间存储器，存储上一次tick的时间
function updateAngle() {
    //计算上一次tick到现在的时间间隔
    let g_now = Date.now();
    let timeDiff = g_now - g_last;
    //根据时间间隔计算移动距离、角度，以及小鸟当前的角度
    distance = MOVE_VELOCITY * timeDiff / 1000.0;
    angle = ROT_VELOCITY * timeDiff / 1000.0 * angleOne;
    birdAngle = (birdAngle + (ANGLE_STEP * timeDiff) / 1000.0) % 360;
    g_last = g_now;
}
function showDebugMassage() {
    dbgmsg.innerHTML = "message:<br/>" +
        "position : : " + eye.elements[0].toFixed(1) + " , " + eye.elements[1].toFixed(1) + " , " + eye.elements[2].toFixed(1) + "<br/>" +
        "look at : : " + at.elements[0].toFixed(1) + " , " + at.elements[1].toFixed(1) + " , " + at.elements[2].toFixed(1) + "<br/>";
}