FROM openjdk:8
MAINTAINER diviz.dev
ADD /var/ChatBot/chatBot.jar /var/ChatBot/chatBot.jar
CMD java -jar /var/ChatBot/chatBot.jar
EXPOSE 5000