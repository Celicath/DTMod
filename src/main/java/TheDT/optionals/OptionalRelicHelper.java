package TheDT.optionals;

import TheDT.patches.CardColorEnum;
import TheDT.relics.MythicalSkillbook;
import basemod.BaseMod;

public class OptionalRelicHelper {
	public static void registerAspirationRelic() {
		BaseMod.addRelicToCustomPool(new MythicalSkillbook(), CardColorEnum.DT_ORANGE);
	}
}
