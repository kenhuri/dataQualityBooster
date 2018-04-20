import { BaseEntity } from './../../shared';

export const enum Status {
    'STARTED',
    'FINISHED',
    'EXCUTING',
    'ERROR'
}

export class ExecutionDataBooster implements BaseEntity {
    constructor(
        public id?: number,
        public startDate?: any,
        public endDate?: any,
        public status?: Status,
        public inputFileContentType?: string,
        public inputFile?: any,
        public outputFileContentType?: string,
        public outputFile?: any,
        public logFileContentType?: string,
        public logFile?: any,
        public optimize?: boolean,
        public train?: boolean,
        public allocation?: boolean,
        public commentary?: any,
        public userId?: string,
        public contextId?: number,
    ) {
        this.optimize = false;
        this.train = false;
        this.allocation = false;
    }
}
