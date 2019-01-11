function [a_quan]=ula_pcm(a,n,u)
%ULA_PCM 	u-law PCM encoding of a sequence
%       	[A_QUAN]=MULA_PCM(X,N,U).
%       	X=input sequence.
%       	n=number of quantization levels (even).     	
%		a_quan=quantized output before encoding.
%       U the parameter of the u-law

% todo:
%ȡ��ԭ���������ֵ
a_max=max(abs(a));
%����u��ѹ��
y=ulaw(a,u);
%ѹ����ĺ������о�������
y_upcm=u_pcm(y,n);
%����ȡ��
x_inv=inv_ulaw(y_upcm,u);
%��ԭ��ԭ�ȵĴ�С
a_quan=a_max.*x_inv;
end
