export function toFirstLetterUppercase(text: string): string {
  return text ? text.charAt(0).toUpperCase() + text.slice(1).toLowerCase() : "";
}

// Formats the date to a string.
export function toDateString(date: Date): string {
  return date
    ? date.toLocaleDateString("de-DE", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
      })
    : "";
}

// Formats the time to a string.
export function toTimeString(date: Date): string {
  return date ? date.toLocaleTimeString() : "";
}

// Formats the date to a string. If the date is today, only the time is returned, otherwise the date is returned.
export function toDateTimeString(date: Date): string {
  if (!date) return "";
  if (date.toDateString() === new Date().toDateString()) {
    return toTimeString(date);
  }
  return toDateString(date);
}

// Formats the number to a string with the given number of fraction digits and optional currency.
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

export function toDateAndTimeString(date: Date): string {
  return date ? date.toLocaleString() : "";
}
