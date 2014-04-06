package com.kindredprints.android.sdk.fragments;

import java.util.LinkedList;

import com.kindredprints.android.sdk.R;
import com.kindredprints.android.sdk.customviews.NavBarView;
import com.kindredprints.android.sdk.customviews.NetworkProgressBar;
import com.kindredprints.android.sdk.data.UserObject;
import com.kindredprints.android.sdk.helpers.prefs.UserPrefHelper;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class KindredFragmentHelper {
	public static final String FRAG_CART = "kp_fragment_cart";
	public static final String FRAG_LOGIN = "kp_fragment_login";
	public static final String FRAG_SHIPPING = "kp_fragment_shipping";
	public static final String FRAG_SHIPPING_EDIT = "kp_fragment_shipping_edit";
	public static final String FRAG_ORDER_SUMMARY = "kp_order_summary";
	public static final String FRAG_ORDER_CARD_EDIT = "kp_order_card_edit";
	public static final String FRAG_ORDER_FINISHED = "kp_order_finished";
	
	
	private UserPrefHelper userPrefHelper_;
	private String currFragHash_;
	private FragmentManager fManager_;
	private LinkedList<String> backStack_;
	private NavBarView navBarView_;
	private Resources resources_;
	private Activity activity_;
	
	private NetworkProgressBar progBar_;
	
	private NextButtonPressInterrupter nextInterrupted_;
	private BackButtonPressInterrupter backInterrupted_;
	
	private static KindredFragmentHelper fragmentHelper_;
	
	public KindredFragmentHelper(Activity activity) {
		this.backStack_ = new LinkedList<String>();
		this.activity_ = activity;
	}
	
	public static KindredFragmentHelper getInstance() {
		return fragmentHelper_;
	}
	
	public static KindredFragmentHelper getInstance(FragmentManager fManager, NavBarView navBarView, Activity activity) {
		if (fragmentHelper_ == null) {
			fragmentHelper_ = new KindredFragmentHelper(activity);
		}
		fragmentHelper_.navBarView_ = navBarView;
		fragmentHelper_.fManager_ = fManager;
		fragmentHelper_.resources_ = activity.getResources();
		return fragmentHelper_;
	}
	
	public void updateActivity(Activity activity) {
		this.activity_ = activity;
		this.userPrefHelper_ = new UserPrefHelper(activity);
		this.progBar_ = new NetworkProgressBar(activity);
	}
	
	public void setNextButtonDreamCatcher_(NextButtonPressInterrupter interrupter_) {
		this.nextInterrupted_ = interrupter_;
	}
	
	public void setBackButtonDreamCatcher_(BackButtonPressInterrupter interrupter_) {
		this.backInterrupted_ = interrupter_;
	}
	
	public void setNextButtonEnabled(boolean enabled) {
		this.navBarView_.setNextButtonEnabled(enabled);
	}
	
	public void triggerNextButton() {
		this.navBarView_.triggerNextButton();
	}
	
	public void showProgressBarWithMessage(String message) {
		this.progBar_.show(message, 0.5f);
	}
	
	public void updateProgressBarWithProgress(float progress) {
		this.progBar_.change_progress(progress);
	}
	
	public void updateProgressBarWithMessage(String message) {
		this.progBar_.change_message(message);
	}
	
	public void hideProgressBar() {
		this.progBar_.hide();
	}
	
	public void initRootFragment() {
		this.currFragHash_ = FRAG_CART;
		KindredFragment f = fragForHash(FRAG_CART);
		f.initFragment(this, this.activity_);
		moveRightFragment(f);
	}
	
	public void configNavBar() {
		configNavBarForHash(this.currFragHash_);
	}
	
	public boolean moveToFragment(String hash) {
		return moveToFragmentWithBundle(hash, new Bundle());
	}
	
	public boolean moveToFragmentWithBundle(String hash, Bundle bun) {
		KindredFragment f = fragForHash(hash);
		if (f != null) {
			f.setArguments(bun);
			f.initFragment(this, this.activity_);
			this.backStack_.add(this.currFragHash_);
			this.currFragHash_ = hash;
			moveRightFragment(f);
			return true;
		}
		return false;
	}
	
	public boolean replaceCurrentFragmentWithFragmentAndBundle(String hash, Bundle bun) {
		KindredFragment f = fragForHash(hash);
		if (f != null) {
			f.setArguments(bun);
			f.initFragment(this, this.activity_);
			this.currFragHash_ = hash;
			moveRightFragment(f);
			return true;
		}
		return false;
	}
	
	public boolean replaceCurrentFragmentWithFragment(String hash) {
		return replaceCurrentFragmentWithFragmentAndBundle(hash, new Bundle());
	}
	
	public boolean moveNextFragmentWithBundle(Bundle bun) {
		if (this.nextInterrupted_ != null) {
			if (this.nextInterrupted_.interruptNextButton()) {
				return true;
			}
		} 
		
		String nextFrag = null;
		if (this.currFragHash_.equals(FRAG_CART)) {			
			if (this.userPrefHelper_.getUserObject().getId().equals(UserObject.USER_VALUE_NONE)) {
				nextFrag = FRAG_LOGIN;
			} else {
				nextFrag = FRAG_SHIPPING;
				if (userPrefHelper_.getAllAddresses().size() == 0) {
					nextFrag = FRAG_SHIPPING_EDIT;
					this.currFragHash_ = FRAG_SHIPPING;
				}
			}
		} else if (this.currFragHash_.equals(FRAG_LOGIN)) {
			nextFrag = FRAG_SHIPPING;
			if (userPrefHelper_.getAllAddresses().size() == 0) {
				nextFrag = FRAG_SHIPPING_EDIT;
				this.currFragHash_ = FRAG_SHIPPING;
			}
		} else if (this.currFragHash_.equals(FRAG_SHIPPING_EDIT)) {
			return moveLastFragmentWithBundle(bun);
		} else if (this.currFragHash_.equals(FRAG_SHIPPING)) {
			nextFrag = FRAG_ORDER_SUMMARY;
		} else if (this.currFragHash_.equals(FRAG_ORDER_SUMMARY)) {
			nextFrag = FRAG_ORDER_FINISHED;
		} else if (this.currFragHash_.equals(FRAG_ORDER_CARD_EDIT)) {
			return moveLastFragmentWithBundle(bun);
		}
		if (nextFrag != null) {
			KindredFragment f = fragForHash(nextFrag);
			if (f != null) {
				f.setArguments(bun);
				f.initFragment(this, this.activity_);
				this.backStack_.add(this.currFragHash_);
				this.currFragHash_ = nextFrag;
				moveRightFragment(f);
				return true;
			}
		}
		return false;
	}
	
	public boolean moveNextFragment() {
		return moveNextFragmentWithBundle(new Bundle());
	}
	
	public boolean moveLastFragmentWithBundle(Bundle bun) {
		if (this.backInterrupted_ != null) {
			if (this.backInterrupted_.interruptBackButton()) {
				return true;
			}
		}
		
		if (!this.backStack_.isEmpty()) {
			this.currFragHash_ = this.backStack_.removeLast();
			Log.i("KindredNav", "moving back to fragment hash " + this.currFragHash_);
			if (this.currFragHash_.equals(FRAG_LOGIN) && !this.userPrefHelper_.getUserObject().getId().equals(UserObject.USER_VALUE_NONE)) {
				this.currFragHash_ = this.backStack_.remove();
			}
			KindredFragment f = fragForHash(this.currFragHash_);
			f.initFragment(this, this.activity_);
			f.setArguments(bun);
			moveLeftFragment(f);
			return true;
		} else {
			KindredFragment f = fragForHash(this.currFragHash_);
			FragmentTransaction ft = this.fManager_.beginTransaction();
			if (f instanceof CartViewPagerFragment) {
				CartViewPagerFragment frag = (CartViewPagerFragment)f;
				frag.cleanUp();
			}
			ft.remove(f);
			ft.commit();
		}
		return false;
	}
	
	public boolean moveLastFragment() {
		return moveLastFragmentWithBundle(new Bundle());
	}
	
	private void moveRightFragment(KindredFragment f) {
		setNextButtonEnabled(true);
		FragmentTransaction ft = this.fManager_.beginTransaction();
		ft.setCustomAnimations(R.anim.anim_slide_in, R.anim.anim_slide_out);
		ft.replace(R.id.fragmentHolder, f, this.currFragHash_);
		ft.commit();
	}
	
	private void moveLeftFragment(KindredFragment f) {
		setNextButtonEnabled(true);
		FragmentTransaction ft = this.fManager_.beginTransaction();
		ft.setCustomAnimations(R.anim.anim_slide_in_back, R.anim.anim_slide_out_back);
		ft.replace(R.id.fragmentHolder, f, this.currFragHash_);
		ft.commit();
	}
	
	private KindredFragment fragForHash(String hash) {
		KindredFragment f = null;
		if (hash.equals(FRAG_CART)) {
			f = (KindredFragment) this.fManager_.findFragmentByTag(FRAG_CART);
			if (f == null) {
				Log.i("KindredSDK", "initting a new cart view pager fragment!!");
				return new CartViewPagerFragment();
			}
		} else if (hash.equals(FRAG_LOGIN)) {
			f = (KindredFragment) this.fManager_.findFragmentByTag(FRAG_LOGIN);
			if (f == null) {
				return new LoginViewFragment();
			}
		} else if (hash.equals(FRAG_SHIPPING)) {
			f = (KindredFragment) this.fManager_.findFragmentByTag(FRAG_SHIPPING);
			if (f == null) {
				return new ShippingListFragment();
			}
		} else if (hash.equals(FRAG_SHIPPING_EDIT)) {
			f = (KindredFragment) this.fManager_.findFragmentByTag(FRAG_SHIPPING_EDIT);
			if (f == null) {
				return new ShippingEditFragment();
			}
		} else if (hash.equals(FRAG_ORDER_SUMMARY)) {
			f = (KindredFragment) this.fManager_.findFragmentByTag(FRAG_ORDER_SUMMARY);
			if (f == null) {
				return new OrderSummaryFragment();
			}
		} else if (hash.equals(FRAG_ORDER_CARD_EDIT)) {
			f = (KindredFragment) this.fManager_.findFragmentByTag(FRAG_ORDER_CARD_EDIT);
			if (f == null) {
				return new CardEditFragment();
			}
		} else if (hash.equals(FRAG_ORDER_FINISHED)) {
			f = (KindredFragment) this.fManager_.findFragmentByTag(FRAG_ORDER_FINISHED);
			if (f == null) {
				return new OrderCompleteFragment();
			}
		}
		return f;
	}
	
	public void configNavBarForHash(String hash) {
		if (hash.equals(FRAG_CART)) {
			this.navBarView_.setNextTitle(this.resources_.getString(R.string.nav_next_title_cart));
			this.navBarView_.setNavTitle(this.resources_.getString(R.string.nav_title_cart));
		} else if (hash.equals(FRAG_LOGIN)) {
			this.navBarView_.setNextTitle(this.resources_.getString(R.string.nav_next_title_login));
			this.navBarView_.setNavTitle(this.resources_.getString(R.string.nav_title_login));
		} else if (hash.equals(FRAG_LOGIN+String.valueOf(LoginViewFragment.STATE_NEED_PASSWORD))) {
			this.navBarView_.setNextTitle(this.resources_.getString(R.string.nav_next_title_login));
			this.navBarView_.setNavTitle(this.resources_.getString(R.string.nav_title_login));
		} else if (hash.equals(FRAG_LOGIN+String.valueOf(LoginViewFragment.STATE_WRONG_PASSWORD))) {
			this.navBarView_.setNextTitle(this.resources_.getString(R.string.nav_next_title_login_reset));
			this.navBarView_.setNavTitle(this.resources_.getString(R.string.nav_title_login));
		} else if (hash.equals(FRAG_SHIPPING)) {
			this.navBarView_.setNextTitle(this.resources_.getString(R.string.nav_next_title_shipping));
			this.navBarView_.setNavTitle(this.resources_.getString(R.string.nav_title_shipping));
		} else if (hash.equals(FRAG_SHIPPING_EDIT)) {
			this.navBarView_.setNextTitle(this.resources_.getString(R.string.nav_next_title_edit_shipping));
			this.navBarView_.setNavTitle(this.resources_.getString(R.string.nav_title_edit_shipping));
		} else if (hash.equals(FRAG_ORDER_SUMMARY) || hash.equals(FRAG_ORDER_CARD_EDIT)) {
			this.navBarView_.setNextTitle(this.resources_.getString(R.string.nav_next_title_purchase));
			this.navBarView_.setNavTitle(this.resources_.getString(R.string.nav_title_purchase));
		} else if (hash.equals(FRAG_ORDER_FINISHED)) {
			this.navBarView_.setNextTitle("");
			this.navBarView_.setNavTitle("");
		}
	}
	
	public interface NextButtonPressInterrupter {
		public boolean interruptNextButton();
	}
	
	public interface BackButtonPressInterrupter {
		public boolean interruptBackButton();
	}
}
