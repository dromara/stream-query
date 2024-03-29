import { BScrollInstance } from './Instance';
import { Options, DefOptions, OptionsConstructor } from './Options';
import Scroller from './scroller/Scroller';
import { ApplyOrder, EventEmitter } from '@better-scroll/shared-utils';
import { UnionToIntersection } from './utils/typesHelper';
interface PluginCtor {
    pluginName: string;
    applyOrder?: ApplyOrder;
    new (scroll: BScroll): any;
}
interface PluginItem {
    name: string;
    applyOrder?: ApplyOrder.Pre | ApplyOrder.Post;
    ctor: PluginCtor;
}
interface PluginsMap {
    [key: string]: boolean;
}
interface PropertyConfig {
    key: string;
    sourceKey: string;
}
declare type ElementParam = HTMLElement | string;
export interface MountedBScrollHTMLElement extends HTMLElement {
    isBScrollContainer?: boolean;
}
export declare class BScrollConstructor<O = {}> extends EventEmitter {
    static plugins: PluginItem[];
    static pluginsMap: PluginsMap;
    scroller: Scroller;
    options: OptionsConstructor;
    hooks: EventEmitter;
    plugins: {
        [name: string]: any;
    };
    wrapper: HTMLElement;
    content: HTMLElement;
    [key: string]: any;
    static use(ctor: PluginCtor): typeof BScrollConstructor;
    constructor(el: ElementParam, options?: Options & O);
    setContent(wrapper: MountedBScrollHTMLElement): {
        valid: boolean;
        contentChanged: boolean;
    };
    private init;
    private applyPlugins;
    private handleAutoBlur;
    private eventBubbling;
    private refreshWithoutReset;
    proxy(propertiesConfig: PropertyConfig[]): void;
    refresh(): void;
    enable(): void;
    disable(): void;
    destroy(): void;
    eventRegister(names: string[]): void;
}
export interface BScrollConstructor extends BScrollInstance {
}
export interface CustomAPI {
    [key: string]: {};
}
declare type ExtractAPI<O> = {
    [K in keyof O]: K extends string ? DefOptions[K] extends undefined ? CustomAPI[K] : never : never;
}[keyof O];
export declare function createBScroll<O = {}>(el: ElementParam, options?: Options & O): BScrollConstructor & UnionToIntersection<ExtractAPI<O>>;
export declare namespace createBScroll {
    var use: typeof BScrollConstructor.use;
    var plugins: PluginItem[];
    var pluginsMap: PluginsMap;
}
declare type createBScroll = typeof createBScroll;
export interface BScrollFactory extends createBScroll {
    new <O = {}>(el: ElementParam, options?: Options & O): BScrollConstructor & UnionToIntersection<ExtractAPI<O>>;
}
export declare type BScroll<O = Options> = BScrollConstructor<O> & UnionToIntersection<ExtractAPI<O>>;
export declare const BScroll: BScrollFactory;
export {};
