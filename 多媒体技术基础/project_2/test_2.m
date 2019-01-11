image=imread('test.jpg');%读取图片
%按照公式进行灰度转换
imageGray=image(:,:,1)*0.29900+image(:,:,2)*0.58700+image(:,:,3)*0.11400;
%将灰度图片写入保存
imwrite(imageGray,'testGray.jpg');
%计算标准差，平方后得到方差，并输出
variance=(std2(imageGray))^2;
fprintf('方差：%f\n',variance);