package TheDT.powers;

import TheDT.DTModMain;
import TheDT.Interfaces.RecoloredPower;
import TheDT.actions.DragonChangeStanceAction;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.stances.NeutralStance;

public class DragonLoseStancePower extends AbstractPower implements RecoloredPower {
	public static final String RAW_ID = "DragonLoseStancePower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private AbstractPlayer d;

	public DragonLoseStancePower(AbstractPlayer dragon, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = dragon;
		this.d = dragon;
		this.amount = amount;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = true;
		this.loadRegion("anger");
	}

	@Override
	public Color getIconColor() {
		return new Color(1.0f, 0.3f, 0.3f, 1.0f);
	}

	@Override
	public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
		super.renderIcons(sb, x, y, new Color(1.0f, 0.3f, 0.3f, c.a));
	}

	@Override
	public void updateDescription() {
		if (amount == 1) {
			description = DESCRIPTIONS[0] + DESCRIPTIONS[3];
		} else {
			description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3];
		}
	}

	@Override
	public void atStartOfTurn() {
		if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			if (this.amount == 0) {
				addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
			} else {
				addToBot(new ReducePowerAction(owner, owner, POWER_ID, 1));
			}
		}
	}

	@Override
	public void onRemove() {
		addToTop(new DragonChangeStanceAction(d, NeutralStance.STANCE_ID));
	}
}
