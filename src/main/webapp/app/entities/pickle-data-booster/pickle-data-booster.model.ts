import { BaseEntity } from './../../shared';

export class PickleDataBooster implements BaseEntity {
    constructor(
        public id?: number,
        public nameField?: string,
        public fileContentType?: string,
        public file?: any,
        public path?: string,
        public contextId?: number,
    ) {
    }
}
