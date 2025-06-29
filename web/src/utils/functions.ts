import type { DateTimeOptions } from "vue-i18n"

export const getExperienceYears = (careerStart: string) => {
    return Math.abs(new Date().getFullYear() - new Date(careerStart).getFullYear())
}

export const getExperienceYearsText = (careerStart: string, yearText: string = 'year', yearsText: string = 'years') => {
    const years = getExperienceYears(careerStart)
    return years === 1 ? `${years} ${yearText}` : `${years} ${yearsText}`
}

export const formatDateRange = (startDate: string, endDate: string, fromText: string = "From", toText:string = "to") => {
    const start = new Date(startDate);
    const end = new Date(endDate);
    
    const options = { day: 'numeric' as const, month: 'short' as const };
    const startOptions: DateTimeOptions = { ...options, year: start.getFullYear() !== end.getFullYear() ? 'numeric' as const : undefined };
    const endOptions: DateTimeOptions = { ...options, year: 'numeric' as const };

    const startFormatted = start.toLocaleDateString('fr-FR', startOptions);
    const endFormatted = end.toLocaleDateString('fr-FR', endOptions);

    return `${fromText} ${startFormatted} ${toText} ${endFormatted}`;
}