package smol.commands

import smol.*
import smol.util.*
import arc.struct.*
import dev.kord.core.*
import dev.kord.core.entity.*
import dev.kord.core.behavior.*
import kotlinx.coroutines.*

object Commands{
    private val pref = "sm!"
    val registry = ObjectMap<String, (Pair<Message, Array<String>>) -> Unit>()
    
    fun command(name: String, proc: (Pair<Message, Array<String>>) -> Unit){
        registry.put(pref + name, proc)
    }
    
    fun process(msg: Message){
        if(registry.containsKey(msg.content.trim().split(' ')[0])){
            var base = msg.content.trim().split(' ').toTypedArray()
            if(base.size > 1){
                var args = base.copyWithoutFirstElement()
            
                if(registry.containsKey(base[0])) registry[base[0]](Pair(msg, args))
            }else{
                if(registry.containsKey(base[0])) registry[base[0]](Pair(msg, arrayOf<String>()))
            }
        }
    }
    
    fun load(){
        command("ping"){
            Vars.client.launch{
                it.first.reply{
                    content = buildString{
                        appendNewline("Pong!")
                        if(it.second.size > 0){
                            append("Some arguments were detected! ( ")
                            it.second.forEach{ append("$it ") }
                            append(")")
                        }
                    }
                }
            }
        }
    }
}