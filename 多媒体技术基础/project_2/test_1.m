%清空输入区
close all; clear all; clc
fileName='';%文件名，初始为空

%用户输入文件名，判断文件名是否符合条件（是否不为空，是否存在对应文件，是否是jpg格式）
while isempty(fileName) || ~endsWith(fileName,'.jpg') || ~exist(fileName,'file')
    fileName=input("请输入图片文件名(后缀为.jpg)\n",'s');
end
image=imread(fileName);%读取图片
[y,x,z]=size(image);
inputX=-1;%初始化用户输入X
inputY=-1;%初始化用户输入Y

%获取用户输入X，Y，并判断X,Y是否在图片像素范围内，符合条件跳出循环，不符合条件继续输入
while (inputX<1) || (inputX>x) || (inputY<1) || (inputY>y)
    inputXY=str2num(input("请输入X和Y坐标,空格或逗号分开(1<=X<="+x+',1<=Y<='+y+')\n','s'));
    %判断用户是否输入两个数字
    if isempty(inputXY)|| size(inputXY,1)~=1||size(inputXY,2)~=2
        inputX=-1;
        inputY=-1;
    else
        inputX=inputXY(1);
        inputY=inputXY(2);
    end
end

tempX=inputX-1;
tempY=inputY-1;
index=1;
%计算需要输出点的坐标，过滤不在像素范围内的点，例如输入 1 1，那么只输出4个点，其他的不需要输出
for i=1:3
    %如果Y不在范围，直接跳过
    if tempY>=1 && tempY<=y
        for j=1:3
            %如果X不在范围，跳过
            if tempX>=1 && tempX<=x
                %像素范围内，输出该点结果
                fprintf('(%d,%d):(%d,%d,%d)\n',tempX,tempY,image(tempY,tempX,1),image(tempY,tempX,2),image(tempY,tempX,3));
            end
            tempX=tempX+1;
        end
    end
    tempY=tempY+1;
    tempX=inputX-1;
end