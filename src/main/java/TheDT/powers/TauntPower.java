package TheDT.powers;

import TheDT.DTModMain;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.vfx.FlashTargetArrowEffect;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static TheDT.utils.StringHelper.highlightedText;

public class TauntPower extends AbstractPower {
	public static final String RAW_ID = "TauntPower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public AbstractCreature tauntTarget;

	public TauntPower(AbstractCreature owner, AbstractCreature tauntTarget) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.tauntTarget = tauntTarget;
		this.updateDescription();
		this.type = PowerType.DEBUFF;
		this.isTurnBased = true;
		this.loadRegion("lockon");
	}

	@Override
	public void flash() {
		super.flash();
		if (DTModMain.enemyTargetDisplayConfig[0]) {
			AbstractDungeon.effectsQueue.add(new FlashTargetArrowEffect(owner, tauntTarget));
		}
	}

	@Override
	public void flashWithoutSound() {
		super.flashWithoutSound();
		if (DTModMain.enemyTargetDisplayConfig[1]) {
			AbstractDungeon.effectsQueue.add(new FlashTargetArrowEffect(owner, tauntTarget, 0.5f));
		}
	}

	@Override
	public void onRemove() {
		super.onRemove();
		if (DTModMain.enemyTargetDisplayConfig[0]) {
			AbstractDungeon.effectsQueue.add(new FlashTargetArrowEffect(owner, DragonTamer.getLivingDragon() == null ? AbstractDungeon.player : ((DragonTamer) AbstractDungeon.player).front));
		}
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0];
		if (tauntTarget instanceof AbstractPlayer) {
			if (tauntTarget instanceof Dragon) {
				description += DESCRIPTIONS[2];
			} else {
				description += DESCRIPTIONS[1];
			}
		} else {
			description += highlightedText(tauntTarget.name);
			if (DESCRIPTIONS.length >= 6 && tauntTarget.name.length() > 0) {
				int code = tauntTarget.name.charAt(tauntTarget.name.length() - 1);
				description += (code - 0xAC00) % 28 == 0 ? DESCRIPTIONS[5] : DESCRIPTIONS[4];
			}
		}
		description += DESCRIPTIONS[3];
	}

	@Override
	public void atEndOfRound() {
		addToBot(new RemoveSpecificPowerAction(owner, owner, TauntPower.POWER_ID));
	}
}
