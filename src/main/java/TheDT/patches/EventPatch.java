package TheDT.patches;

import TheDT.DTModMain;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;

public class EventPatch {
	@SpirePatch(clz = EventHelper.class, method = "getEvent")
	public static class GetEvent {
		public static AbstractEvent Postfix(AbstractEvent __result, String key) {
			if (DTModMain.isAspirationLoaded && RelicLibrary.getRelic(DTModMain.MythicalSkillbookID).canSpawn()) {
				return __result;//new BookOnTheFloor();
			} else {
				return __result;
			}
		}
	}
}
