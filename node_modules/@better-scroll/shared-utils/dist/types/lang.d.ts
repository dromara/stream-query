export declare function getNow(): number;
export declare const extend: <T extends object, U extends object>(target: T, source: U) => T & U;
export declare function isUndef(v: any): boolean;
export declare function getDistance(x: number, y: number): number;
export declare function between(x: number, min: number, max: number): number;
export declare function findIndex<T>(ary: T[], fn: (value: T, index: number, arr?: T[]) => boolean): number;
