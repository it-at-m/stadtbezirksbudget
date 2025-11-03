/**
 * Converts the first letter of the text to uppercase and the rest to lowercase.
 * Returns an empty string if the text is empty or undefined.
 */
export function toFirstLetterUppercase(text: string): string {
  return text ? text.charAt(0).toUpperCase() + text.slice(1).toLowerCase() : "";
}

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
  return validateDate(date) ? date.toLocaleTimeString() : "";
}

/**
 * Formats the date. Returns the time if it's today, otherwise returns the date.
 * Returns an empty string if the date is invalid.
 */
export function toDateTimeString(date: Date): string {
  if (!validateDate(date)) return "";
  const today = new Date();
  if (date.toDateString() === today.toDateString()) {
    return toTimeString(date);
  }
  return toDateString(date);
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
 * Formats the date to a string including both date and time.
 * Returns an empty string if the date is invalid.
 */
export function toDateAndTimeString(date: Date): string {
  return validateDate(date) ? date.toLocaleString("de-DE") : "";
}

/**
 * Validates if the provided date is a valid Date object.
 */
function validateDate(date: Date): boolean {
  return date && !isNaN(date.getTime());
}

/**
 * Converts a boolean value to a string representation.
 * Returns "Fehl" for true, "Fest" for false, or an empty string for null/undefined.
 */
export function booleanToString(value: boolean): string {
  if (value == null) return "";
  return value ? "Fehl" : "Fest";
}
