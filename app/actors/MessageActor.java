package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.FeedResponse;
import data.Message;
import data.NewsAgentResponse;
import services.FeedService;
import services.NewsAgentService;

import java.util.UUID;

import static data.Message.Sender.BOT;
import static data.Message.Sender.USER;

public class MessageActor extends UntypedActor {
    //define another actor reference
    private final ActorRef out;

    //self-reference the actor
    public MessageActor(ActorRef out) {
        this.out = out;
    }

    //props
    public static Props props(ActorRef out) {
        return Props.create(MessageActor.class, out);
    }

    //object of feed service
    public FeedService feedService = new FeedService();
    //object of NewsAgentService
    public NewsAgentService newsAgentService = new NewsAgentService();


    @Override
    public void onReceive(Object message) throws Throwable {
        ObjectMapper objectMapper = new ObjectMapper();
        Message messageObject = new Message();
        //send back the message
        if (message instanceof String) {
            //create the message object for the received message
            messageObject.text = (String) message;
            messageObject.sender = USER;
            out.tell(objectMapper.writeValueAsString(messageObject), self());
            //call the news agent service
            String query = newsAgentService.getNewsAgentResponse(messageObject.text, UUID.randomUUID()).query;
            //call feed service
            FeedResponse feedResponse = feedService.getFeedQuery(query);
            //send the reply to the user
            //create the message object for the response to the sender
            messageObject.text = (feedResponse.title == null) ? "No results found" : "Showing results for " + query;
            messageObject.sender = BOT;
            messageObject.feedResponse = feedResponse;
            out.tell(objectMapper.writeValueAsString(messageObject), self());


        }


    }
}