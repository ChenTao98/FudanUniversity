function [a_quan]=u_pcm(a,n)
%U_PCM  	uniform PCM encoding of a sequence
%       	[A_QUAN]=U_PCM(A,N)
%       	a=input sequence.
%       	n=number of quantization levels (even).
%		a_quan=quantized output before encoding.

% todo:
a_max=max(a);%量化的最大值
a_min=min(a);%量化的最小值
interval=(a_max-a_min)/n;%量化取值的区间间隔
quantify_value=zeros(1,n+1);%存储量化取值区间的端点
for i=1:n+1
    quantify_value(i)=a_min+(interval*(i-1));%初始化量化取值区间
end
a_length=length(a);
a_quan=zeros(1,a_length);%初始化返回向量
%开始量化
for i=1:a_length
    %获取当前需要量化的值
    temp=a(i);
    for j=1:n
        %判断原始值所在的区间，输出量化值，量化值为区间的中点
        if(quantify_value(j)<=temp&&temp<quantify_value(j+1))
            a_quan(i)=(quantify_value(j)+quantify_value(j+1))/2;
            break;
        elseif(temp>=quantify_value(n+1))
           a_quan(i)=(quantify_value(n)+quantify_value(n+1))/2;
           break;
        end
    end
end
        
end
