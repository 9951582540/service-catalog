FROM nginx
RUN rm -rf /etc/nginx/conf.d/default.conf
COPY default.conf /etc/nginx/conf.d/default.conf
RUN rm -rf /usr/share/nginx/html/*
COPY service-catalogue-ui-1.0.0 /usr/share/nginx/html
