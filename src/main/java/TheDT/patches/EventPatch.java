package TheDT.patches;

import TheDT.events.BookOnTheFloor;
import TheDT.relics.MythicalSkillbook;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;

public class EventPatch {
	@SpirePatch(clz = EventHelper.class, method = "getEvent")
	public static class GetEvent {
		public static AbstractEvent Postfix(AbstractEvent __result, String key) {
			if (RelicLibrary.getRelic(MythicalSkillbook.ID).canSpawn()) {
				return new BookOnTheFloor();
			} else {
				return __result;
			}
		}
	}
}
