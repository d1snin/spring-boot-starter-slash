package uno.d1s.slash.listener.replier

import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction

public interface DeferReplier {

    public fun reply(action: ReplyCallbackAction): ReplyCallbackAction
}