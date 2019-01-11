function [z]=ulaw(y,u)
%		u-law nonlinearity for nonuniform PCM
%		X=ULAW(Y,U).
%		Y=input vector.

% todo:
%uбия╧кУ
y_max=max(abs(y));
z=(log(1+u*abs(y/y_max))/log(1+u)).*sign(y);
end