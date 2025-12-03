// Type definition of sorting diretion
export type sortDirection = "asc" | "desc";

// Interface for Antrag list sorting options
export interface AntragListSortOption {
    title: string;
    sortBy: string;
    sortDirection: sortDirection;
}

// Type definition of AntragListSort based of {@link sortOptionsRecord}
export type AntragListSort = Record<string, AntragListSortOption | undefined>;

// Creates an empty AntragListSort object
export const createEmptyListSort = (): AntragListSort => {
    return Object.keys(sortOptionsRecord).reduce((acc, key) => {
        acc[key] = undefined;
        return acc;
    }, {} as AntragListSort);
};

// Defines which sorting options are available for different values
const sortOptionsRecord: Record<string, Omit<AntragListSortOption, "sortBy">[]> = {
    status: [
        {
            title: "A-Z / Aufsteigend",
            sortDirection: "asc",
        },
        {
            title: "Z-A / Absteigend",
            sortDirection: "desc",
        },
    ],
    nummer: [
        {
            title: "Aufsteigend",
            sortDirection: "asc",
        },
        {
            title: "Absteigend",
            sortDirection: "desc",
        },
    ],
    aktenzeichen: [
        {
            title: "Aufsteigend",
            sortDirection: "asc",
        },
        {
            title: "Absteigend",
            sortDirection: "desc",
        },
    ],
    bezirk: [
        {
            title: "Aufsteigend",
            sortDirection: "asc",
        },
        {
            title: "Absteigend",
            sortDirection: "desc",
        },
    ],
    antragsdatum: [
        {
            title: "Zuerst hinzugefügt / Aufsteigend",
            sortDirection: "asc",
        },
        {
            title: "Zuletzt hinzugefügt / Absteigend",
            sortDirection: "desc",
        },
    ],
    antragsteller: [
        {
            title: "A-Z / Aufsteigend",
            sortDirection: "asc",
        },
        {
            title: "Z-A / Absteigend",
            sortDirection: "desc",
        },
    ],
    projekt: [
        {
            title: "A-Z / Aufsteigend",
            sortDirection: "asc",
        },
        {
            title: "Z-A / Absteigend",
            sortDirection: "desc",
        },
    ],
    beantragtesBudget: [
        {
            title: "Aufsteigend",
            sortDirection: "asc",
        },
        {
            title: "Absteigend",
            sortDirection: "desc",
        },
    ],
    art: [
        {
            title: "A-Z / Aufsteigend",
            sortDirection: "asc",
        },
        {
            title: "Z-A / Absteigend",
            sortDirection: "desc",
        },
    ],
    aktualisierungArt: [
        {
            title: "A-Z / Aufsteigend",
            sortDirection: "asc",
        },
        {
            title: "Z-A / Absteigend",
            sortDirection: "desc",
        },
    ],
    aktualisierungDatum: [
        {
            title: "Zuerst aktualisiert / Aufsteigend",
            sortDirection: "asc",
        },
        {
            title: "Zuletzt aktualisiert / Absteigend",
            sortDirection: "desc",
        },
    ],
};

// Populates the {@link sortOptionsRecord} with the corresponding key as sortBy param
export const sortOptionsByField = (field: keyof typeof sortOptionsRecord): AntragListSortOption[] =>
    sortOptionsRecord[field]?.map((value) => ({...value, sortBy: field})) || [];

