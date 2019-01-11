function [a_quan]=ula_pcm(a,n,u)
%ULA_PCM 	u-law PCM encoding of a sequence
%       	[A_QUAN]=MULA_PCM(X,N,U).
%       	X=input sequence.
%       	n=number of quantization levels (even).     	
%		a_quan=quantized output before encoding.
%       U the parameter of the u-law

% todo:
%取得原函数的最大值
a_max=max(abs(a));
%进行u律压缩
y=ulaw(a,u);
%压缩后的函数进行均匀量化
y_upcm=u_pcm(y,n);
%量化取逆
x_inv=inv_ulaw(y_upcm,u);
%还原成原先的大小
a_quan=a_max.*x_inv;
end
