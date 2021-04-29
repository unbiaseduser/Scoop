package tk.wasdennnoch.scoop.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import tk.wasdennnoch.scoop.Intents
import tk.wasdennnoch.scoop.R
import tk.wasdennnoch.scoop.util.copyTextToClipboard
import tk.wasdennnoch.scoop.util.displayToast

class ShareReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val stackTrace = intent?.getStringExtra("stackTrace")
        val pkg = intent?.getStringExtra("pkg")
        when (intent?.action) {
            Intents.INTENT_ACTION_SHARE -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, stackTrace)
                context.startActivity(
                    Intent.createChooser(shareIntent, context.getString(R.string.action_share))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
            Intents.INTENT_ACTION_COPY -> {
                context.copyTextToClipboard(R.string.copy_label, pkg, stackTrace)
                context.displayToast(R.string.copied_toast)
            }
            Intents.INTENT_ACTION_COPY_LINK -> {
                context.copyTextToClipboard(
                    R.string.copy_link_label,
                    pkg,
                    intent.getStringExtra(Intents.INTENT_DOGBIN_LINK)
                )
                context.displayToast(R.string.copied_link_toast)
            }
        }
    }
}