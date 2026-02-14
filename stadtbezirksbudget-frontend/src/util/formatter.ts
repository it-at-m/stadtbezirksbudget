/**
 * Formats the date to a string in the German locale (de-DE).
 * Returns an empty string if the date is invalid.
 */
export function toDateString(date: Date): string {
  return validateDate(date)
    ? date.toLocaleDateString("de-DE", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
      })
    : "";
}

/**
 * Formats the date to a string representing the time.
 * Returns an empty string if the date is invalid.
 */
export function toTimeString(date: Date): string {
  return validateDate(date) ? date.toLocaleTimeString("de-DE") : "";
}

/**
 * Formats the date to a local ISO string (YYYY-MM-DDTHH:mm:ss).
 * @param date - The date to format.
 * @returns The formatted date string or undefined if the date is invalid.
 */
export function toLocalISOString(
  date: Date | undefined | null
): string | undefined {
  if (!date || !validateDate(date)) return undefined;
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");
  return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
}

/**
 * Formats the number to a string with specified fraction digits and optional currency.
 */
export function toNumberString(
  amount: number,
  fractionDigits = 2,
  currency?: string
): string {
  const options = {
    minimumFractionDigits: fractionDigits,
    maximumFractionDigits: fractionDigits,
  };
  if (currency) {
    Object.assign(options, { style: "currency", currency: currency });
  }
  return amount.toLocaleString("de-DE", options);
}

/**
 * Validates if the provided date is a valid Date object.
 */
function validateDate(date: Date): boolean {
  return date && !isNaN(date.getTime());
}
