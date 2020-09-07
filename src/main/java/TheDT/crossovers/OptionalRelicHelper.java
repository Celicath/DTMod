package TheDT.crossovers;

import TheDT.relics.DragonTamerSkillbook;
import basemod.BaseMod;
import basemod.helpers.RelicType;

public class OptionalRelicHelper {
	public static void registerAspirationRelic() {
		BaseMod.addRelic(new DragonTamerSkillbook(), RelicType.SHARED);
	}
}
