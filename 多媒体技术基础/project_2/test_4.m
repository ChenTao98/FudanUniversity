image=imread('test.jpg');%读取图片
%如果不是灰度图，转化为灰度图
if size(image,3)==3
    image=rgb2gray(image);
end
%加上椒盐噪声
imageNoise=imnoise(image,'salt & pepper',0.04);
%中值滤波去噪
imageMedian=medfilt2(imageNoise,[3 3]);
%均值滤波去噪
imageAverage=imfilter(imageNoise,fspecial('average',[3 3]));
%将噪声图片与去噪图片保存
imwrite(imageNoise,'椒盐噪声图.jpg');
imwrite(imageMedian,'中值滤波图.jpg');
imwrite(imageAverage,'均值滤波图.jpg');