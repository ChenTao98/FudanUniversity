function [a_quan]=u_pcm(a,n)
%U_PCM  	uniform PCM encoding of a sequence
%       	[A_QUAN]=U_PCM(A,N)
%       	a=input sequence.
%       	n=number of quantization levels (even).
%		a_quan=quantized output before encoding.

% todo:
a_max=max(a);%���������ֵ
a_min=min(a);%��������Сֵ
interval=(a_max-a_min)/n;%����ȡֵ��������
quantify_value=zeros(1,n+1);%�洢����ȡֵ����Ķ˵�
for i=1:n+1
    quantify_value(i)=a_min+(interval*(i-1));%��ʼ������ȡֵ����
end
a_length=length(a);
a_quan=zeros(1,a_length);%��ʼ����������
%��ʼ����
for i=1:a_length
    %��ȡ��ǰ��Ҫ������ֵ
    temp=a(i);
    for j=1:n
        %�ж�ԭʼֵ���ڵ����䣬�������ֵ������ֵΪ������е�
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
