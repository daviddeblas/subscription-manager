export interface EmailData {
    id: string;
    subject: string;
    from: string;
    unsubscribeLinks?: string[];
    brandName?: string;
    fromAddress?: string;
    unsubscribeLink?: string;
}
