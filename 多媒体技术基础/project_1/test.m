t=0:0.0001:2*pi;
y=cos(t);
z1=u_pcm(y,64);
z2=ula_pcm(y,64,255);
plot(t,y,t,z1,'r',t,z2,'g');