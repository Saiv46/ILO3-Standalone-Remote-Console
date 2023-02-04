import com.hp.ilo2.intgapp.intgapp
import util.Http
import java.util.*
import kotlin.system.exitProcess

/*
 * For some reason, Java applet does not work (ILO 1.94 and FF 91.8.0esr)
 *
 * This program mimics https://github.com/scrapes/ILO2-Standalone-Remote-Console/ closely since ILO3 is basically
 * ILO2, but with json requests/responses
 *
 * VM args: -Djava.security.properties=java.security
 *
 * Notes:
 *      Regex find and replace: getLocalString\((.*?)\) -> $1
 */

/** Global Variables **/
private val USAGE_TEXT: String = """
    Usage: 
    - ILO3RemCon.jar <Hostname or IP> <Session Key>
    """.trimIndent()

/** Main Variables **/
private var session = ""
private var hostname = ""

fun main(args: Array<String>) {
    when (args.size) {
        2 -> {
            hostname = args[0]
            session = args[1]
        }
        else -> {
            println(USAGE_TEXT)
            return
        }
    }

    if (hostname.isBlank() || session.isBlank()) {
        println("hostname, or session is blank!")
        exitProcess(1)
    }

    Http.sessionKey = session

    // Everything should be OK now. Connect to the applet.
    intgapp(hostname).run {
        init()
        start()
    }
}

