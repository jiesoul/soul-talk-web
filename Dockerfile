FROM nginx

MAINTAINER jiesoul <jiesoul@gmail.com>

RUN rm /etc/nginx/conf.d/default.conf

COPY nginx-site.conf /etc/nginx/conf.d/default.conf

RUN mkdir /app

COPY resources/public/ /app/

RUN mkdir /app/cljs

COPY target/public/cljs/app.js /app/cljs/app.js

#COPY start-container.sh /usr/local/bin/start-container
#
#ENTRYPOINT ["start-container"]
#
#CMD ["nginx"]



