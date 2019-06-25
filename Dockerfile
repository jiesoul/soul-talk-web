FROM nginx

MAINTAINER jiesoul <jiesoul@gmail.com>

RUN rm /etc/nginx/conf.d/default.conf

COPY nginx-site.conf /etc/nginx/conf.d/default.conf

RUN mkdir /app

COPY resources/public/ /app/

RUN mkdir /app/cljs

COPY target/public/cljs/app.js /app/cljs/app.js

#COPY site-entrypoint.sh /app/site-entrypoint.sh
#
#EXPOSE 80
#
#ENTRYPOINT ["/app/site-entrypoint.sh"]
#
#CMD ["nginx"]



