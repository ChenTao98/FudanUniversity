%���������
close all; clear all; clc
fileName='';%�ļ�������ʼΪ��

%�û������ļ������ж��ļ����Ƿ�����������Ƿ�Ϊ�գ��Ƿ���ڶ�Ӧ�ļ����Ƿ���jpg��ʽ��
while isempty(fileName) || ~endsWith(fileName,'.jpg') || ~exist(fileName,'file')
    fileName=input("������ͼƬ�ļ���(��׺Ϊ.jpg)\n",'s');
end
image=imread(fileName);%��ȡͼƬ
[y,x,z]=size(image);
inputX=-1;%��ʼ���û�����X
inputY=-1;%��ʼ���û�����Y

%��ȡ�û�����X��Y�����ж�X,Y�Ƿ���ͼƬ���ط�Χ�ڣ�������������ѭ����������������������
while (inputX<1) || (inputX>x) || (inputY<1) || (inputY>y)
    inputXY=str2num(input("������X��Y����,�ո�򶺺ŷֿ�(1<=X<="+x+',1<=Y<='+y+')\n','s'));
    %�ж��û��Ƿ�������������
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
%������Ҫ���������꣬���˲������ط�Χ�ڵĵ㣬�������� 1 1����ôֻ���4���㣬�����Ĳ���Ҫ���
for i=1:3
    %���Y���ڷ�Χ��ֱ������
    if tempY>=1 && tempY<=y
        for j=1:3
            %���X���ڷ�Χ������
            if tempX>=1 && tempX<=x
                %���ط�Χ�ڣ�����õ���
                fprintf('(%d,%d):(%d,%d,%d)\n',tempX,tempY,image(tempY,tempX,1),image(tempY,tempX,2),image(tempY,tempX,3));
            end
            tempX=tempX+1;
        end
    end
    tempY=tempY+1;
    tempX=inputX-1;
end