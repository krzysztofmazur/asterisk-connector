FROM ubuntu:15.10
MAINTAINER krz@ychu.pl

RUN \
    apt-get update && \
    apt-get -y upgrade && \
    apt-get install -y nano mc htop && \
    apt-get install -y asterisk && \
    apt-get install -y supervisor

ADD asterisk/manager.conf /etc/asterisk/manager.conf
ADD asterisk/manager.d/admin.conf /etc/asterisk/manager.d/admin.conf
RUN chmod 666 /etc/asterisk/manager.conf
RUN chmod 666 /etc/asterisk/manager.d/admin.conf

ENV HOME /root
ENV TERM xterm

EXPOSE 5038