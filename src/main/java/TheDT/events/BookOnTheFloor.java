package TheDT.events;

import TheDT.DTModMain;
import TheDT.relics.DragonTamerSkillbook;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;

public class BookOnTheFloor extends AbstractImageEvent {
	public static final String RAW_ID = "BookOnTheFloor";
	public static final String ID = DTModMain.makeID(RAW_ID);
	private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
	public static final String NAME = eventStrings.NAME;
	public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
	public static final String[] OPTIONS = eventStrings.OPTIONS;
	private BookOnTheFloor.CUR_SCREEN screen;

	private enum CUR_SCREEN {
		INTRO,
		COMPLETE
	}

	public BookOnTheFloor() {
		super(NAME, DESCRIPTIONS[0], DTModMain.GetEventPath(RAW_ID));

		this.screen = BookOnTheFloor.CUR_SCREEN.INTRO;
		this.imageEventText.setDialogOption(OPTIONS[0]);
	}

	protected void buttonEffect(int buttonPressed) {
		switch (this.screen) {
			case INTRO:
				this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
				AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), new DragonTamerSkillbook());
				AbstractEvent.logMetricObtainRelic(ID, "Obtain Book", new DragonTamerSkillbook());
				this.imageEventText.updateDialogOption(0, OPTIONS[1]);
				this.imageEventText.clearRemainingOptions();

				this.screen = BookOnTheFloor.CUR_SCREEN.COMPLETE;
				break;
			case COMPLETE:
				this.openMap();
		}
	}
}
