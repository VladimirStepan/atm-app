type ReverseMap<T extends Record<PropertyKey, PropertyKey>> = {
    [V in T[keyof T]]: {
        [K in keyof T]: T[K] extends V ? K : never
    }[keyof T]
};

export const idToCurrency = {
    0: 'rub',
    1: 'usd',
    2: 'eur'
} as const;

export const currencyToId = Object.fromEntries(
    Object.entries(idToCurrency).map(([k, v]) => [v, k])
) as unknown as CurrencyToId;

export type CurrencyCode = keyof typeof idToCurrency;
export type Currency = (typeof idToCurrency)[CurrencyCode];
export type CurrencyToId = ReverseMap<typeof idToCurrency>;