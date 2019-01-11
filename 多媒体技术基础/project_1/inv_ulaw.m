function x=inv_ulaw(y,u)
%INV_ULAW		the inverse of u-law nonlinearity
%X=INV_ULAW(Y,U)	X=normalized output of the u-law nonlinearity.

% todo:
%u��ѹ���������
x=(((1+u).^abs(y)-1)/u).*sign(y);
end