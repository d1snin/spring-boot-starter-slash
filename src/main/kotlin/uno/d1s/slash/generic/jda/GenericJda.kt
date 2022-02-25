package uno.d1s.slash.generic.jda

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.sharding.ShardManager

public class GenericJda<T>(private val api: T) {

    private val unsupportedApi = IllegalStateException("${api!!::class.simpleName} is not supported.")

    init {
        if (api !is JDA && api !is ShardManager) {
            throw unsupportedApi
        }

        if (api is ShardManager) {
            api.shards.forEach {
                it.awaitReady()
            }
        }
    }

    internal fun getGuildById(id: String): Guild = this.callApi({
        getGuildById(id)
    }) {
        getGuildById(id)
    } ?: throw IllegalArgumentException("Guild was not found.")

    internal fun upsertCommands(commands: Set<CommandData>) {
        this.callApi({
            updateCommands().addCommands(commands).queue()

        }) {
            shards.first().updateCommands().addCommands(commands)
                .queue()
        }
    }

    private fun <R> callApi(jdaBlock: JDA.() -> R, shardManagerBlock: ShardManager.() -> R): R =
        when (api) {
            is JDA -> jdaBlock(api)
            is ShardManager -> shardManagerBlock(api)
            else -> throw unsupportedApi
        }
}
