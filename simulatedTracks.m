clear

A = 5;
n= 20;

noffs = rand(n,6,3)*2000; % Initial offset for each set of polynomials
posOffs = rand(n,3)*200; % Random start position offset

%% Creating multi-component polynomials
m1 = [5.8594E-13, -1.3411E-09, 1.0026E-06, -2.5885E-04, 1.1458E-02];
m2 = [-1.3021E-13, 1.0417E-10, 1.7188E-07, -1.9167E-04, 4.5833E-02];
m3 = [1.1719E-12, -2.9948E-09, 2.7135E-06, -1.0177E-03, 1.2708E-01];

x = 0:1000;
y1 = m1(1)*x.^5 + m1(2)*x.^4 + m1(3)*x.^3 + m1(4)*x.^2 + m1(5)*x;
y2 = m2(1)*x.^5 + m2(2)*x.^4 + m2(3)*x.^3 + m2(4)*x.^2 + m2(5)*x;
y3 = m3(1)*x.^5 + m3(2)*x.^4 + m3(3)*x.^3 + m3(4)*x.^2 + m3(5)*x;

y1 = [y1(1:end-1),-flip(y1(2:end-1),2)];
y2 = [y2(1:end-1),-flip(y2(2:end-1),2)];
y3 = [y3(1:end-1),-flip(y3(2:end-1),2)];

x = 1:1999;

for ni = 1:n
    ys1 = A*(...
        circshift(y1,[0,round(noffs(ni,1,1))])...
        + circshift(y2,[0,round(noffs(ni,2,1))])...
        + circshift(y3,[0,round(noffs(ni,3,1))])...
        + circshift(y1,[0,round(noffs(ni,4,1))])...
        + circshift(y2,[0,round(noffs(ni,5,1))])...
        + circshift(y3,[0,round(noffs(ni,6,1))]));
    
    ys2 = A*(...
        circshift(y1,[0,round(noffs(ni,1,2))])...
        + circshift(y2,[0,round(noffs(ni,2,2))])...
        + circshift(y3,[0,round(noffs(ni,3,2))])...
        + circshift(y1,[0,round(noffs(ni,4,2))])...
        + circshift(y2,[0,round(noffs(ni,5,2))])...
        + circshift(y3,[0,round(noffs(ni,6,2))]));
    
    ys3 = A*(...
        circshift(y1,[0,round(noffs(ni,1,3))])...
        + circshift(y2,[0,round(noffs(ni,2,3))])...
        + circshift(y3,[0,round(noffs(ni,3,3))])...
        + circshift(y1,[0,round(noffs(ni,4,3))])...
        + circshift(y2,[0,round(noffs(ni,5,3))])...
        + circshift(y3,[0,round(noffs(ni,6,3))]));
    
    pos((ni-1)*1000+1:(ni-1)*1000+1000,1) = ni;
    pos((ni-1)*1000+1:(ni-1)*1000+1000,2) = 1:1000;
    pos((ni-1)*1000+1:(ni-1)*1000+1000,3) = round(posOffs(ni,1)+ys1(1:1000));
    pos((ni-1)*1000+1:(ni-1)*1000+1000,4) = round(posOffs(ni,2)+ys2(1:1000));
    pos((ni-1)*1000+1:(ni-1)*1000+1000,5) = round(posOffs(ni,3)+ys3(1:1000));
       
    plot3(posOffs(ni,1)+ys1(1:1000),posOffs(ni,2)+ys2(1:1000),posOffs(ni,3)+ys3(1:1000));
    hold on
    
end

% nFr = 100;
% im = zeros(400,400,400,nFr,'uint8');
% 
% for ni = 1:n
%     for fr = 1:nFr
%         im(pos(fr*5,1,ni),pos(fr*5,2,ni),pos(fr*5,3,ni),fr) = 65535;
%     end
% end
% 
% for i = 1:200
%     for j = 1:nFr
%        imwrite(im(:,:,i,j),'test.tif','WriteMode','append');
%     end
% end
