image=imread('test.jpg');%��ȡͼƬ
%���չ�ʽ���лҶ�ת��
imageGray=image(:,:,1)*0.29900+image(:,:,2)*0.58700+image(:,:,3)*0.11400;
%���Ҷ�ͼƬд�뱣��
imwrite(imageGray,'testGray.jpg');
%�����׼�ƽ����õ���������
variance=(std2(imageGray))^2;
fprintf('���%f\n',variance);