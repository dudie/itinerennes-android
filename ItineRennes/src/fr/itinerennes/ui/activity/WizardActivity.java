package fr.itinerennes.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ViewSwitcher;

import fr.itinerennes.R;

/**
 * A base activity to set up configuration wizards.
 * <p>
 * Subclasses must define a default constructor and call {@link WizardActivity#WizardActivity(int)}.
 * <br>
 * You must also implement {@link #onShow(int)}.
 * <p>
 * You may want to override {@link #onCancel()}, {@link #onPrevious(int)}, {@link #onNext(int)} and
 * {@link #onFinish()}.
 * 
 * @author Jérémie Huchet
 */
/**
 * @author Jérémie Huchet
 */
public abstract class WizardActivity extends ItineRennesActivity {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(WizardActivity.class);

    /** Constant indicating that the wizard switch back to a previous step. */
    private static final int TO_PREVIOUS_STEP = -1;

    /** Constant indicating that we don't know if the wizard switch back or forward to a step. */
    private static final int NO_DIRECTION = 0;

    /** Constant indicating that the wizard switch forward to a next step. */
    private static final int TO_NEXT_STEP = 1;

    /** The buttons to navigate in the wizard. */
    private Button leftButton, rightButton;

    /** The view containing the steps views. */
    private ViewSwitcher stepsContainer;

    /** The current step number. */
    private int currentStep = 0;

    /** The step adapters to use to display steps. */
    private final List<WizardStepAdapter> stepsAdapters = new ArrayList<WizardStepAdapter>(5);

    /**
     * If the back button is pressed, instead of exiting the activity we go back to the previous
     * wizard step until we reach the first step. If the back button in pressed one more time then
     * the default behavior is executed.
     * <p>
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {

        if (KeyEvent.KEYCODE_BACK == keyCode && currentStep > 0) {
            LOGGER.debug("BACK pressed but wizard is on step {}, moving back to step {}",
                    currentStep, currentStep - 1);
            show(--currentStep, TO_PREVIOUS_STEP);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * Initializes the wizard.
     * <p>
     * This shouldn't be overrided, but if you do then be sure to call
     * {@link WizardActivity#onCreate(Bundle)} before doing anything.
     * <p>
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {

        LOGGER.debug("onCreate.start");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_wizard_base);

        stepsContainer = (ViewSwitcher) findViewById(R.id.act_wizard_steps);
        leftButton = (Button) findViewById(R.id.act_wizard_button_left);
        rightButton = (Button) findViewById(R.id.act_wizard_button_right);

        final WizardActivityButtonsClickListener buttonsListener = new WizardActivityButtonsClickListener();
        leftButton.setOnClickListener(buttonsListener);
        rightButton.setOnClickListener(buttonsListener);

        onCreateSteps(savedInstanceState);

        if (stepsAdapters.size() <= 0) {
            throw new IllegalArgumentException(
                    String.format("the wizard must have one or more steps"));
        }

        show(0, NO_DIRECTION);

        LOGGER.debug("onCreate.end");
    }

    /**
     * Subsclasses should use this method to initialize wizard steps calling
     * {@link #addStep(WizardStepAdapter)}.
     * 
     * @param savedInstanceState
     *            see {@link #onCreate(Bundle)}
     */
    protected abstract void onCreateSteps(Bundle savedInstanceState);

    /**
     * Subclasses should use this method to add steps to the wizard.
     * 
     * @param stepAdapter
     *            a step adapter
     */
    protected final void addStep(final WizardStepAdapter stepAdapter) {

        stepsAdapters.add(stepAdapter);
    }

    /**
     * Click listener to attach to wizard's buttons.
     * 
     * @author Jérémie Huchet
     */
    private final class WizardActivityButtonsClickListener implements OnClickListener {

        /**
         * Trigerred when the user clicks on the wizard's buttons.
         * <p>
         * Result will cause the wizard to quit or switch to another step.
         * <p>
         * {@inheritDoc}
         * 
         * @see android.view.View.OnClickListener#onClick(android.view.View)
         */
        @Override
        public void onClick(final View v) {

            final WizardStepAdapter stepAdapter = stepsAdapters.get(currentStep);

            switch (v.getId()) {
            case R.id.act_wizard_button_left:
                // LEFT button click
                if (0 == currentStep) {
                    stepAdapter.onCancel();
                } else {
                    stepAdapter.onPrevious();
                    show(--currentStep, TO_PREVIOUS_STEP);
                }
                break;
            case R.id.act_wizard_button_right:
                // RIGHT button click
                if (stepsAdapters.size() - 1 == currentStep) {
                    stepAdapter.onFinish();
                } else {
                    stepAdapter.onNext();
                    show(++currentStep, TO_NEXT_STEP);
                }
                break;
            default:
                LOGGER.warn("can't handle event on view {}", this.getClass().getName(), v.getId());
                break;
            }
        }
    }

    /**
     * Show the step identified by the given identifier.
     * <p>
     * Valid identifiers are 0 <= id < stepCount. See {@link WizardActivity#WizardActivity(int)} to
     * define the total amount of steps.
     * 
     * @param stepId
     *            the wizard step number to display
     * @param direction
     *            an extra information to understand from/to which side we are going to set
     *            animations. See {@link WizardActivity#TO_PREVIOUS_STEP},
     *            {@link WizardActivity#TO_NEXT_STEP} and {@link WizardActivity#NO_DIRECTION}
     */
    private void show(final int stepId, final int direction) {

        if (LOGGER.isDebugEnabled()) {
            switch (direction) {
            case TO_NEXT_STEP:
                LOGGER.debug("displaying step {} switching forward ->", stepId);
                break;
            case TO_PREVIOUS_STEP:
                LOGGER.debug("displaying step {} switching back <-", stepId);
                break;
            case NO_DIRECTION:
                LOGGER.debug("displaying step {}, no direction", stepId);
                break;
            default:
                LOGGER.warn("unknown direction: {}", direction);
                break;
            }
        }

        final WizardStepAdapter step = stepsAdapters.get(stepId);

        /*
         * A ViewSwitcher can only contain 2 views. When switching from a step to another, we have
         * to remove one child. Assuming we reach step_4 from step_3 : the WiewSwitcher contains
         * views for step_3 and step_4. ----------------------------------------------------------
         * We want to switch to step_5, then we have to remove the view of the setp_3 before adding
         * the view of the step_5.
         */

        // we have to remove the view which is not currently displayed
        // eg. we have to remove the view at index != stepsContainer.getDisplayedChild()
        // and we know there are 2 children max
        // childIndexToRemove = (childIndexDisplayed + 1) % 2
        final int childIndexToRemove = (stepsContainer.getDisplayedChild() + 1) % 2;
        if (stepsContainer.getChildCount() > 1) {
            stepsContainer.removeViewAt(childIndexToRemove);
        } else if (NO_DIRECTION == direction) {
            // remove all views if there is no directions (wizard is opened for instance)
            stepsContainer.removeAllViews();
        }
        stepsContainer.addView(step.onShow(stepId));

        // set animations
        if (TO_PREVIOUS_STEP == direction) {
            // we are switching from right to left <<
            // so the new view has to be before the old one
            // stepsContainer.setOutAnimation(AnimationUtils.loadAnimation(WizardActivity.this,
            // android.R.anim.slide_out_right));
            // stepsContainer.setInAnimation(AnimationUtils.loadAnimation(WizardActivity.this,
            // android.R.anim.slide_in_left));

        } else if (TO_NEXT_STEP == direction) {
            // we are switching from left to right >>
            // so the new view has to be after the old one
            // stepsContainer.setOutAnimation(AnimationUtils.loadAnimation(WizardActivity.this,
            // R.anim.slide_in_right));
            // stepsContainer.setInAnimation(AnimationUtils.loadAnimation(WizardActivity.this,
            // R.anim.slide_out_left));

        }

        // set buttons labels
        leftButton.setText(step.getLeftButtonLabel());
        rightButton.setText(step.getRightButtonLabel());

        stepsContainer.showNext();
    }

    /**
     * Adapter to expose wizard steps in a {@link WizardActivity}.
     * 
     * @author Jérémie Huchet
     */
    public interface WizardStepAdapter {

        /**
         * This method is invoked to request the view to display one of the wizard steps.
         * 
         * @param stepId
         *            the number of the step requested
         * @return the view to display in this wizard step
         */
        View onShow(int stepId);

        /**
         * This is invoked when the user clicks button cancel of the wizard.
         */
        void onCancel();

        /**
         * This is invoked when the user clicks button <i>previous</i>.
         */
        void onPrevious();

        /**
         * This is invoked when the user clicks button <i>next</i>.
         */
        void onNext();

        /**
         * This is invoked when the user clicks button finish at the end of the wizard.
         */
        void onFinish();

        /**
         * Gets the text label to set on the left button of this step.
         * 
         * @return the text label to set on the left button of this step
         */
        String getLeftButtonLabel();

        /**
         * Gets the text label to set on the right button of this step.
         * 
         * @return the text label to set on the right button of this step
         */
        String getRightButtonLabel();
    }

    /**
     * A base implementation of {@link WizardStepAdapter}.
     * 
     * @author Jérémie Huchet
     */
    public abstract class BaseWizardStepAdapter implements WizardStepAdapter {

        /**
         * {@inheritDoc}
         * 
         * @see fr.itinerennes.ui.activity.WizardActivity.WizardStepAdapter#onShow(int)
         */
        @Override
        public abstract View onShow(int stepId);

        /**
         * {@inheritDoc}
         * <p>
         * Default behavior is to {@link #finish()} the activity.
         * 
         * @see fr.itinerennes.ui.activity.WizardActivity.WizardStepAdapter#onCancel()
         */
        @Override
        public void onCancel() {

            finish();
        }

        /**
         * {@inheritDoc}
         * 
         * @see fr.itinerennes.ui.activity.WizardActivity.WizardStepAdapter#onPrevious()
         */
        @Override
        public void onPrevious() {

        }

        /**
         * {@inheritDoc}
         * 
         * @see fr.itinerennes.ui.activity.WizardActivity.WizardStepAdapter#onNext()
         */
        @Override
        public void onNext() {

        }

        /**
         * {@inheritDoc}
         * <p>
         * Default behavior is to {@link Activity#finish()} the activity.
         * 
         * @see fr.itinerennes.ui.activity.WizardActivity.WizardStepAdapter#onFinish()
         */
        @Override
        public void onFinish() {

            finish();
        }

        /**
         * {@inheritDoc}
         * <p>
         * The left button label is set to "Cancel" if the step is the first one, else it is set to
         * "Previous".
         * 
         * @see fr.itinerennes.ui.activity.WizardActivity.WizardStepAdapter#getLeftButtonLabel()
         */
        @Override
        public String getLeftButtonLabel() {

            if (stepsAdapters.indexOf(this) == 0) {
                return WizardActivity.this.getString(R.string.cancel);
            } else {
                return WizardActivity.this.getString(R.string.prev);
            }
        }

        /**
         * {@inheritDoc}
         * <p>
         * The right button label is set to "Finish" if the step is the last one, else it is set to
         * "Next".
         * 
         * @see fr.itinerennes.ui.activity.WizardActivity.WizardStepAdapter#getRightButtonLabel()
         */
        @Override
        public String getRightButtonLabel() {

            if (stepsAdapters.indexOf(this) == stepsAdapters.size() - 1) {
                return WizardActivity.this.getString(R.string.finish);
            } else {
                return WizardActivity.this.getString(R.string.next);
            }
        }
    }
}
