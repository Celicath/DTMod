package TheDT.relics;

import TheDT.DTModMain;
import TheDT.characters.DragonTamer;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class MagicStone extends CustomRelic {

	public static final String RAW_ID = "MagicStone";
	public static final String ID = DTModMain.makeID(RAW_ID);
	public static final String IMG = DTModMain.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTModMain.GetRelicOutlinePath(RAW_ID);
	public static final int AMOUNT = 7;

	public MagicStone() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.BOSS, LandingSound.SOLID);
	}

	@Override
	public void obtain() {
		if (AbstractDungeon.player.hasRelic(PactStone.ID)) {
			for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
				if (AbstractDungeon.player.relics.get(i).relicId.equals(PactStone.ID)) {
					instantObtain(AbstractDungeon.player, i, true);
					break;
				}
			}
		} else {
			super.obtain();
		}
	}

	@Override
	public void atBattleStart() {
		this.flash();
		addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
		addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, AMOUNT));
		if (AbstractDungeon.player instanceof DragonTamer) {
			DragonTamer dt = (DragonTamer) AbstractDungeon.player;
			addToBot(new RelicAboveCreatureAction(dt.dragon, this));
			addToBot(new HealAction(dt.dragon, dt.dragon, AMOUNT));
		}
	}

	@Override
	public boolean canSpawn() {
		return AbstractDungeon.player.hasRelic(PactStone.ID);
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new MagicStone();
	}
}
