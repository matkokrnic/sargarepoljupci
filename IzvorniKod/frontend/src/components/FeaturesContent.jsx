import { ArrowTrendingUpIcon, BanknotesIcon, CalendarDaysIcon, WalletIcon } from '@heroicons/react/24/outline'

const features = [
  {
    name: 'Besplatno za koristiti',
    description:
      "Naša aplikacija nije samo za bogate - svatko je može koristiti, registracija ne potrebna! Bili vi posvećen korisnik ili prolaznik na našoj aplikaciji, svatko ima koristi od nje!",
    icon: BanknotesIcon,
  },
  {
    name: 'Planirajte unaprijed',
    description:
      'S mogućnosti rezerviranja mjesta unaprijed također s opcijom ponavljajuće rezervacije, ne morate se više brinuti gdje se parkirati za Mariju Bistricu ili igdje drugdje!',
    icon: CalendarDaysIcon,
  },
  {
    name: 'Podržite svoj biznis',
    description:
      'Osim korisnika koji traže smještaj za auto, na ovoj aplikaciji dobrodošli su i voditelji vlastitih parkirališta! Prijavite vlastito parkiralište, i uživajte u većoj vidljivosti vašeg aranžmana.',
    icon: ArrowTrendingUpIcon,
  },
  {
    name: 'Online bankarstvo',
    description:
      'Ako se ne volite petljati novčanicama i kovanicama, dobra vijest - ni mi! A s našom aplikacijom, niti se morate! Putem našeg e-novčanika, možete lako, brzo i sigurno platiti parkiralište, bez problema!',
    icon: WalletIcon,
  },
]

export function FeaturesContent() {
  return (
    <div className="py-24 bg-white sm:py-32">
      <div className="px-6 mx-auto max-w-7xl lg:px-8">
        <div className="max-w-2xl mx-auto lg:text-center">
          <h2 className="text-base font-semibold leading-7 text-indigo-600">Your go-to stop to find a stop.</h2>
          <p className="mt-2 text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
          Nađi mjesto za parkirati - sad!
          </p>
          <p className="mt-6 text-lg leading-8 text-gray-600">
           Naša moćna aplikacija će vam naći mjesto za parkirati u sekundama! Uživajte u munjevito brzim algoritmima za pretragu,
           širokom nizu parkirališnih mjesta neovisno o vašoj lokaciji, mogućnosti plaćanja online, i još puno toga!
          </p>
        </div>
        <div className="max-w-2xl mx-auto mt-16 sm:mt-20 lg:mt-24 lg:max-w-4xl">
          <dl className="grid max-w-xl grid-cols-1 gap-x-8 gap-y-10 lg:max-w-none lg:grid-cols-2 lg:gap-y-16">
            {features.map((feature) => (
              <div key={feature.name} className="relative pl-16">
                <dt className="text-base font-semibold leading-7 text-gray-900">
                  <div className="absolute top-0 left-0 flex items-center justify-center w-10 h-10 bg-indigo-600 rounded-lg">
                    <feature.icon className="w-6 h-6 text-white" aria-hidden="true" />
                  </div>
                  {feature.name}
                </dt>
                <dd className="mt-2 text-base leading-7 text-gray-600">{feature.description}</dd>
              </div>
            ))}
          </dl>
        </div>
      </div>
    </div>
  )
}
