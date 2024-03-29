import { Behavior } from './scroller/Behavior';
import Actions from './scroller/Actions';
import { ExposedAPI as ExposedAPIByScroller } from './scroller/Scroller';
import { Animater } from './animater';
import { ExposedAPI as ExposedAPIByAnimater } from './animater/Base';
export interface BScrollInstance extends ExposedAPIByScroller, ExposedAPIByAnimater {
    [key: string]: any;
    x: Behavior['currentPos'];
    y: Behavior['currentPos'];
    hasHorizontalScroll: Behavior['hasScroll'];
    hasVerticalScroll: Behavior['hasScroll'];
    scrollerWidth: Behavior['contentSize'];
    scrollerHeight: Behavior['contentSize'];
    maxScrollX: Behavior['maxScrollPos'];
    maxScrollY: Behavior['maxScrollPos'];
    minScrollX: Behavior['minScrollPos'];
    minScrollY: Behavior['minScrollPos'];
    movingDirectionX: Behavior['movingDirection'];
    movingDirectionY: Behavior['movingDirection'];
    directionX: Behavior['direction'];
    directionY: Behavior['direction'];
    enabled: Actions['enabled'];
    pending: Animater['pending'];
}
export declare const propertiesConfig: {
    sourceKey: string;
    key: string;
}[];
