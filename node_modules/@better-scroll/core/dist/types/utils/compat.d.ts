import { TranslaterPoint } from '../translater';
declare type Position = {
    x: number;
    y: number;
};
export declare const isValidPostion: (startPoint: TranslaterPoint, endPoint: TranslaterPoint, currentPos: Position, prePos: Position) => boolean;
export {};
