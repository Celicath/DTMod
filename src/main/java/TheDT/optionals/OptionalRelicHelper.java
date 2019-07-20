package TheDT.optionals;

import TheDT.relics.MythicalSkillbook;
import basemod.BaseMod;
import basemod.helpers.RelicType;

public class OptionalRelicHelper {
	public static void registerAspirationRelic() {
		BaseMod.addRelic(new MythicalSkillbook(), RelicType.SHARED);
	}
}
