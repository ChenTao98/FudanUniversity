image=imread('test.jpg');%��ȡͼƬ
%������ǻҶ�ͼ��ת��Ϊ�Ҷ�ͼ
if size(image,3)==3
    image=rgb2gray(image);
end
%���Ͻ�������
imageNoise=imnoise(image,'salt & pepper',0.04);
%��ֵ�˲�ȥ��
imageMedian=medfilt2(imageNoise,[3 3]);
%��ֵ�˲�ȥ��
imageAverage=imfilter(imageNoise,fspecial('average',[3 3]));
%������ͼƬ��ȥ��ͼƬ����
imwrite(imageNoise,'��������ͼ.jpg');
imwrite(imageMedian,'��ֵ�˲�ͼ.jpg');
imwrite(imageAverage,'��ֵ�˲�ͼ.jpg');