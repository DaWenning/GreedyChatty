
package chatty.gui.components.textpane;

import chatty.User;
import chatty.util.StringUtil;
import chatty.util.api.Emoticons;
import chatty.util.irc.MsgTags;

/**
 *
 * @author tduva
 */
public class UserNotice extends InfoMessage {
    
    public final String type;
    public final User user;
    public final String attachedMessage;
    public final Emoticons.TagEmotes emotes;
    public final String infoText;
    
    private int msgStart;
    private int msgEnd;
    
    public UserNotice(String type, User user, String infoText, String message,
            MsgTags tags) {
        super(Type.INFO, makeFullText(type, infoText, message, tags), tags);
        this.type = type;
        this.user = user;
        this.attachedMessage = message;
        this.emotes = Emoticons.parseEmotesTag(tags.getRawEmotes());
        this.infoText = infoText;
        
        if (!StringUtil.isNullOrEmpty(message)) {
            if (tags.isValue("msg-id", "announcement")) {
                msgStart = text.length() - message.length();
                msgEnd = text.length();
            }
            else {
                msgStart = text.length() - 1 - message.length();
                msgEnd = text.length() - 1;
            }
        }
    }
    
    /**
     * Create a new UserNotice similiar to the given one, but with different
     * tags.
     *
     * @param other
     * @param tags 
     */
    public UserNotice(UserNotice other, MsgTags tags) {
        this(other.type, other.user, other.infoText, other.attachedMessage, tags);
    }
    
    @Override
    public int getMsgStart() {
        return msgStart;
    }
    
    @Override
    public int getMsgEnd() {
        return msgEnd;
    }
    
    private static String makeFullText(String type, String text, String message, MsgTags tags) {
        if (StringUtil.isNullOrEmpty(message)) {
            return String.format("[%s] %s", type, text);
        }
        if (tags != null && tags.isValue("msg-id", "announcement")) {
            return String.format("[%s] %s%s", type, text, message);
        }
        return String.format("[%s] %s [%s]", type, text, message);
    }
    
}
