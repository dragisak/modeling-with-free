# Modeling Video Store with Free

[![Build Status](https://travis-ci.org/dragisak/modeling-with-free.svg?branch=master)](https://travis-ci.org/dragisak/modeling-with-free)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/5aae2fa1b0f848089c3102313157fb43)](https://www.codacy.com/app/dragisak/modeling-with-free?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dragisak/modeling-with-free&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/5aae2fa1b0f848089c3102313157fb43)](https://www.codacy.com/app/dragisak/modeling-with-free?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dragisak/modeling-with-free&amp;utm_campaign=Badge_Coverage)

Using [Freasy](https://github.com/Thangiee/Freasy-Monad) and [cats](http://typelevel.org/cats/) Free Monads to model a domain.


Freasy macro allows for very concise code:

```scala
@free trait VideoRental {
  sealed trait DSL[A]
  type VideoRentalF[A] = Free[DSL, A]
  def addInventory(movie: Movie, qty: Int): VideoRentalF[Set[DVD]]
  def searchForDVD(movie: Movie): VideoRentalF[Option[DVD]]
  def rentDVD(dvd: DVD): VideoRentalF[Unit]
  def returnDVD(dvd: DVD): VideoRentalF[Unit]
}

```
```scala
override def interpreter(): VideoRental.Interp[ErrorOr] = new VideoRental.Interp[ErrorOr] {
  override def addInventory(movie: Movie, qty: Int): ErrorOr[Set[DVD]] = ...
  override def searchForDVD(movie: Movie): ErrorOr[Option[DVD]] = ...
  override def rentDVD(dvd: DVD): ErrorOr[Unit] = ...
  override def returnDVD(dvd: DVD): ErrorOr[Unit] = ...
}
```
